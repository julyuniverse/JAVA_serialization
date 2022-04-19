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
