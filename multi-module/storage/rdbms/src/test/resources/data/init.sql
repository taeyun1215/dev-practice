CREATE TABLE test.shipments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tracking_number VARCHAR(255) NOT NULL,
    status VARCHAR(100) NOT NULL
);

INSERT INTO test.shipments (tracking_number, status) VALUES ('TN123456789', 'Delivered');
INSERT INTO test.shipments (tracking_number, status) VALUES ('TN987654321', 'Shipped');
INSERT INTO test.shipments (tracking_number, status) VALUES ('TN111223344', 'In Transit');
INSERT INTO test.shipments (tracking_number, status) VALUES ('TN444555666', 'Arrived at Hub');
INSERT INTO test.shipments (tracking_number, status) VALUES ('TN777888999', 'Out for Delivery');
