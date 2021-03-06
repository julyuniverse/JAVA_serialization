# 직렬화(serialization)

## serializtion이란?

- 인스턴스의 상태를 그대로 파일 저장하거나 네트워크로 전송하고 (serializtion) 이를 다시 복원(deserializtion) 하는 방식
- 자바에서는 보조 스트림을 활용하여 직렬화를 제공함.
- ObjectInputStream 보조 스트림과 ObjectOutputStream 보조 스트림

| 생성자 | 설명 |
| --- | --- |
| ObjectInputStream(InputStream in) | InputStream을 생성자의 매개변수로 받아 ObjectInputStream을 생성. |
| ObjectOutputStream(OutputStream out) | OutputStream을 생성자의 매개변수로 받아 ObjectOutputStream을 생성. |

## Serializtion 인터페이스

- 직렬화는 인스턴스의 내용이 외부로 유출되는 것이므로 프로그래머가 해당 객체에 대한 직렬화 의도를 표시해야 함.
- 구현 코드가 없는 marker interface
- transient: 직렬화하지 않으려는 멤버 변수에 사용함 (Socket 등 직렬화할 수 없는 객체)

```java
import java.io.*;

class Person implements Serializable { // Serializable: 직렬화는 인스턴스의 내용이 외부로 유출되는 것이므로 프로그래머가 해당 객체에 대한 직렬화 의도를 표시해야 함.
    String name;
    transient String job; // transient: 직렬화하지 않으려는 멤버 변수에 사용함 (Socket 등 직렬화할 수 없는 객체)

    // 기본 생성자
    public Person() {
    }

    // 매개 변수가 있는 생성자
    public Person(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String toString() {
        return name + ", " + job;
    }
}

public class SerializationTest {
    public static void main(String[] args) {
        Person personLee = new Person("이순신", "대표이사");
        Person personKim = new Person("김유신", "상무이사");

        try (FileOutputStream fos = new FileOutputStream("serial.txt");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(personLee);
            oos.writeObject(personKim);
        } catch (IOException e) {
            System.out.println(e);
        }

        try (FileInputStream fis = new FileInputStream("serial.txt");
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Person pLee = (Person) ois.readObject();
            Person pKim = (Person) ois.readObject();

            System.out.println(pLee);
            System.out.println(pKim);

            // 결과
            // 이순신, null
            // 김유신, null
        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }
}
```

## Externalizable 인터페이스

- writerExternal()과 readExternal() 메서드를 구현해야 함.
- 프로그래머가 직접 객체를 읽고 쓰는 코드를 구현할 수 있음.

```java
import java.io.*;

class Person2 implements Externalizable {
    String name;
    String job;

    // 기본 생성자
    public Person2() {
    }

    // 매개 변수가 있는 생성자
    public Person2(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String toString() {
        return name + ", " + job;
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        // 직렬화할 멤버 변수만 선언하면 [transient: 직렬화하지 않으려는 멤버 변수에 사용함 (Socket 등 직렬화할 수 없는 객체)]과 같은 효과를 낸다.
        objectOutput.writeUTF(name);
        // objectOutput.writeUTF(job);
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        name = objectInput.readUTF();
        // job = objectInput.readUTF();
    }
}

public class SerializationTest2 {
    public static void main(String[] args) {
        Person2 personLee = new Person2("이순신", "대표이사");
        Person2 personKim = new Person2("김유신", "상무이사");

        try (FileOutputStream fos = new FileOutputStream("serial.txt");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(personLee);
            oos.writeObject(personKim);
        } catch (IOException e) {
            System.out.println(e);
        }

        try (FileInputStream fis = new FileInputStream("serial.txt");
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Person2 pLee = (Person2) ois.readObject();
            Person2 pKim = (Person2) ois.readObject();

            System.out.println(pLee);
            System.out.println(pKim);

            // 결과
            // 이순신, null
            // 김유신, null
        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }
}
```