package com.example.demo.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToWriter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SchemaGenerator {

    @PersistenceContext
    private EntityManager entityManager;

    public String generateCreateDDL() {
        StringWriter output = new StringWriter();
        BufferedWriter writer = new BufferedWriter(output);

        try (output; writer) {
            StandardServiceRegistry serviceRegistry = getServiceRegistry();
            MetadataSources metadataSources = getMetadataSources();

            for (Class<?> entity : getEntities()) {
                metadataSources.addAnnotatedClass(entity);
            }

            Metadata metadata = metadataSources.buildMetadata(serviceRegistry);

            SchemaExport schemaExport = new SchemaExport();
            schemaExport.setFormat(true);
            schemaExport.perform(SchemaExport.Action.CREATE, metadata, new ScriptTargetOutputToWriter(writer));

            return output.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Error generating schema DDL", e);
        }
    }

    public String generateDropDDL() {
        StringWriter output = new StringWriter();
        BufferedWriter writer = new BufferedWriter(output);

        try (output; writer) {
            StandardServiceRegistry serviceRegistry = getServiceRegistry();
            MetadataSources metadataSources = getMetadataSources();

            for (Class<?> entity : getEntities()) {
                metadataSources.addAnnotatedClass(entity);
            }

            Metadata metadata = metadataSources.buildMetadata(serviceRegistry);

            SchemaExport schemaExport = new SchemaExport();
            schemaExport.setFormat(true);
            schemaExport.perform(SchemaExport.Action.DROP, metadata, new ScriptTargetOutputToWriter(writer));

            return output.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Error generating schema DDL", e);
        }
    }

    private List<Class<?>> getEntities() {
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        return entities.stream()
                .map(EntityType::getJavaType)
                .collect(Collectors.toList());
    }

    private MetadataSources getMetadataSources() {
        return new MetadataSources(getServiceRegistry());
    }

    private StandardServiceRegistry getServiceRegistry() {
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        return sessionFactory.getSessionFactoryOptions().getServiceRegistry();
    }
}
