### **JPA의 기본 키 생성 전략**

JPA (Java Persistence API)는 객체 관계 매핑 (ORM)을 위한 표준 Java API로, 다음과 같은 네 가지 기본 키 생성 전략을 제공한다.

1. **`AUTO`**: 데이터베이스의 기본 키 생성 전략을 사용한다. 이는 특정 데이터베이스가 지원하는 기본 키 생성 메커니즘에 의존한다.
2. **`IDENTITY`**: 데이터베이스의 IDENTITY 컬럼을 사용하여 PK를 생성한다. 이 전략은 데이터베이스에 엔티티를 삽입한 후 ID 값을 얻는다.
3. **`SEQUENCE`**: 데이터베이스의 시퀀스를 사용하여 PK를 생성한다. 이 전략은 미리 정의된 데이터베이스 시퀀스를 통해 값을 얻는다.
4. **`TABLE`**: 키 생성 전용 테이블을 사용한다. 이 전략은 별도의 테이블에 숫자를 저장하고, 이를 통해 PK 값을 생성한다.

### **JPA 기본 키 생성 전략의 단점**

- **`IDENTITY` 전략의 단점**
    - 성능 저하
        - 온라인 상점에서 대규모 상품 목록을 데이터베이스에 삽입하는 상황을 가정해 보겠다.
        - 이 상점의 데이터베이스는 **`IDENTITY`** 전략을 사용하여 각 상품의 ID를 자동으로 증가시키고 있다.
            
            ```java
            @Transactional
            public void batchInsertProducts(List<Product> products) {
                for (Product product : products) {
                    entityManager.persist(product);
                    // 각 상품 삽입마다 데이터베이스는 AUTO_INCREMENT 값을 찾아서 업데이트합니다.
                }
            }
            ```
            
        - 만약 이 리스트에 수천 개의 상품이 있고, 모든 상품을 순차적으로 삽입해야 한다면, 데이터베이스는 각 삽입 연산마다 **`AUTO_INCREMENT`** 값을 업데이트하기 위해 해당 테이블을 조회해야 한다.
        - 이런 방대한 양의 삽입이 일어날 때, 데이터베이스 서버에는 큰 부하가 걸리고, 결과적으로 상품 삽입 처리 시간이 매우 길어지는 성능 저하를 겪게 된다.
    - 트랜잭션 범위 내 ID 접근 불가
        - 한 트랜잭션에서 사용자와 사용자의 계정을 생성하는 경우를 생각해보자.
        - 사용자 ID가 생성되고 나서야, 해당 ID를 사용하여 계정을 생성해야 한다.
            
            ```java
            @Transactional
            public void createUserAndAccount(UserDTO userDTO) {
                User user = new User();
                user.setName(userDTO.getName());
                userRepository.save(user);
                // 이 시점에서 user.getId()는 null이 될 수 있다.
            
                Account account = new Account();
                account.setUserId(user.getId()); // user ID가 필요하다.
                accountRepository.save(account);
                // user ID는 삽입 이후에야 사용 가능하다.
            }
            ```
            
        - **`IDENTITY`** 전략을 사용하면 **`user`** 엔티티가 실제로 데이터베이스에 삽입될 때까지 ID가 생성되지 않는다.
        - 이는 JPA가 **`user`** 엔티티를 영속성 컨텍스트에 추가하고 트랜잭션을 커밋하는 시점까지 기다려야 한다는 것을 의미한다.
        - 따라서 **`account`** 엔티티에 **`user`**의 ID를 설정하기 전에는 **`user`**가 실제로 데이터베이스에 삽입되었는지 확인하기 어렵다.
        - 이는 특히 계정 생성 로직이 **`user`** 엔티티의 ID에 의존하는 경우 문제가 될 수 있다.
- **`SEQUENCE` 전략의 단점**
    ![image](https://github.com/taeyun1215/dev-practice/assets/65766105/88505a50-0103-45a4-bcb2-d2aef338b155)
    - MySQL 데이터베이스를 사용 중인데, 시스템의 일부를 PostgreSQL로 마이그레이션하면서 **`SEQUENCE`** 전략을 도입한다는 가정
    - PostgreSQL은 **`SEQUENCE`** 객체를 네이티브로 지원하지만, MySQL은 **`SEQUENCE`** 객체를 네이티브로 지원하지 않다.
    - 따라서 MySQL 데이터베이스를 사용하는 환경에서는 **`SEQUENCE`** 전략을 사용할 수 없다.
- **`TABLE` 전략의 단점**
    - **`TABLE`** 전략을 사용하여 사용자 테이블에 대한 PK를 관리하고 있다.
    - 이 전략은 키 생성 전용 테이블을 두어 PK 값을 관리한다.
    - 매번 사용자를 생성할 때마다 키 생성 테이블을 조회하고 업데이트해야 한다.
    - 대용량 트래픽이 발생하는 경우, 이 테이블이 병목 지점이 될 수 있고, 결국 전체 시스템의 성능 저하를 초래할 수 있다.
- **`AUTO` 전략의 단점**
    - 어떤 회사에서 여러 데이터베이스 시스템을 사용한다고 가정하겠다. (예: Oracle, MySQL, PostgreSQL).
    - 개발 팀은 어플리케이션 내에서 JPA **`AUTO`** 전략을 채택하였다.
    - 개발 팀이 공통된 코드 베이스를 사용하여 플랫폼을 개발했음에도 불구하고, 각 데이터베이스에서 **`AUTO`** 전략이 다르게 동작하는 것 발견함.
    - 예를 들어, Oracle은 **`SEQUENCE`**를 사용하는 반면, MySQL은 **`IDENTITY`**를 사용한다.
    - 이로 인해 개발 및 테스트 단계에서 예상치 못한 성능 차이가 발생함.

### **UUID 키 생성 전략**

- UUID (Universally Unique Identifier)는 표준화된 128비트의 숫자를 사용하여 전 세계적으로 고유한 ID를 생성하는 방법이다.
- UUID는 다음과 같은 형식을 갖는다.
    - **`xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`**, 여기서 각 x는 16진수다.
- UUID 생성 전략의 장점은 어떠한 환경에서도 고유한 값을 생성할 수 있다는 것이다.
- 장점
    1. 전 세계적으로 고유한 값을 생성할 수 있으며 충돌 확률이 매우 낮다.
    2. UUID는 순차적이지 않아서, 식별자의 순서로부터 어떠한 정보도 유추할 수 없어 보안 측면에서 유리할 수 있다.
    3. 생성시에 외부 상태에 의존하지 않아서 별도의 관리 없이 독립적으로 생성이 가능하다. → DB 확장성 측면에서 고려해야할 사항들이 줄어듬.
- 단점
    1. UUID는 128비트 길이로 이루어져 있으며, 이는 기본적인 정수 기반의 ID보다 상당히 길다.
    2. 대규모 테이블에서는 UUID가 차지하는 저장 공간이 문제가 될 수 있으며, Auto Increment 정수 기반의 키에 비해 약 4배 더 많은 공간을 차지합니다.
    3. UUID는 생성 시 시간에 기반한 순차성을 가지지 않는다. 이는 특히 B+Tree 인덱스를 사용하는 SQL 데이터베이스에서 단점이 될 수 있다.
    4. 또한, 쿼리 조회시 정렬이 반 필수적으로 이뤄진다.
    5. UUID는 문자열로 표현되기 때문에, 데이터베이스에서 정수와 비교할 때 더 많은 시간이 소요된다. 문자열 비교는 정수 비교보다 성능이 약 2.5배에서 28배 정도 저하될 수 있다.
    6. 관계형 데이터베이스에서 클러스터링된 프라이머리 키를 사용할 경우, 새로운 행이 삽입될 때마다 데이터베이스 엔진이 행을 재정렬해야 합니다. UUID의 무작위성으로 인해 인덱스 페이지 분할과 재정렬이 빈번하게 발생하여 성능이 저하될 수 있습니다.

### **UUID 키 생성 전략(코드)**

- UserUUID
    
    ```java
    @Entity
    public class UserUUID {
    
        @Id
        @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid", strategy = "uuid2")
        private UUID id;
    
        private String name;
        private String email;
    
    }
    ```

### **ULID 키 생성 전략**

- ULID(Universally Unique Lexicographically Sortable Identifier)는 UUID의 대안으로 제안된 식별자이다.
    ![image](https://github.com/taeyun1215/dev-practice/assets/65766105/89ae805a-91da-475d-a5fd-6008e619e146)
- 장점
    1. ULID는 시간 기반의 식별자로, 시간에 따른 정렬이 가능하다.
    2. 이진 형식으로 저장될 때 더 효율적입니다.
    3. UUID에 비해 정렬이나 검색 성능이 좋기에 인덱스의 순차성을 유지하므로 데이터베이스 성능에 덜 부담을 준다.
    4. 생성시에 외부 상태에 의존하지 않아서 별도의 관리 없이 독립적으로 생성이 가능하다. → DB 확장성 측면에서 고려해야할 사항들이 줄어듬.
- 단점
    1. UUID에 비해 상대적으로 새로워서 지원하지 않는 시스템이 있을 수 있다. → 아직은 못 본 문제
    2. ULID 생성과 처리를 위해서는 추가적인 라이브러리 필요하다.

### **ULID 키 생성 전략(코드)**

- UserULID
    
    ```java
    @Entity
    public class UserULID {
    
        @Id
        @GeneratedValue(generator = "ulid-generator")
        @GenericGenerator(name = "ulid-generator", strategy = "com.example.demo.ulid.ULIDGenerator")
        private String id;
    }
    ```
    
- ULIDGenerator
    
    ```java
    public class ULIDGenerator implements IdentifierGenerator {
    
        @Override
        public Serializable generate(SharedSessionContractImplementor session, Object object) {
            return UlidCreator.getUlid().toString();
        }
    }
    ```
    
- build.gradle
    
    ```java
    
    dependencies {
        // ULID
        implementation 'com.github.f4b6a3:ulid-creator:5.2.3'
    }
    
    ```
    

### JPA 기본 키 생성 전략 vs UUID(ULID) 키 생성 전략 선택의 기준

- JPA 기본 키
    1. 테이블이 주로 서버 내부에서 참조되며 클라이언트에게 노출되지 않는 경우, 예를 들어 사용자의 세션 정보나 로그 데이터 등이 이에 해당한다.
    2. 대규모 데이터를 다루는 서비스에서는 인덱싱 성능이 중요할 수 있으며, Auto Increment Key는 데이터베이스의 B-Tree 구조에 최적화되어 있어 UUID보다 더 빠르게 처리할 수 있다.
    3. 특정 엔티티를 순차적으로 생성하고 관리할 필요가 있는 경우에 적합하다.
- UUID(ULID) 키
    1. URL이나 API 응답과 같이 클라이언트에게 노출되고 사용자가 직접 다룰 수 있는 식별자는 UUID를 사용하는 것이 적합하다. 이는 예측이 어렵고, 해킹의 위험을 줄여주기 때문이다. → 예를 들어, 공유된 문서나 공개된 리소스의 식별자로 사용될 수 있다.
    2. 여러 서버 또는 클라이언트가 동시에 데이터를 생성할 때, UUID는 고유한 값을 보장하기 때문에 충돌 없이 식별자를 생성할 수 있다.
    3. 사용자 계정, 결제 정보 등과 같이 보안이 중요한 엔티티에는 UUID를 사용하여 예측 불가능하게 만드는 것이 좋다.
