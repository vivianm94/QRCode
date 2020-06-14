public class Main {
    public static void main(String[]args) throws InvalidInput {
       // System.out.println("Hello World");

        QRCode qw=new QRCode(1,QRCode.ECCLevel.L,10,4);

        qw.CreateQRCode("123456as");

       // qw.IsValidISO("123456as");

    }
}
