public class QRCode {
    private static char[] alphanumEncTable = { ' ', '$', '%', '*', '+', '-', '.', '/', ':' };
    public int version;
    public String errorCorrection;
    public int boxSize;
    public int border;
    public String maskPattern;

    public enum Ecimode{
       Default(0),
       Iso8859_1(3),
       Iso8859_2 (4),
       Utf8(26);

       private int value;

       private Ecimode(int value) {
           this.value = value;
       }
   }

     //Error Correction Level
    // These define the tolerance levels for how much of the code can be lost before the code cannot be recovered.
   public enum ECCLevel
   {
       L,
       M,
       Q,
       H,
   }

    private enum EncodingMode
    {
        Default(0),
        Numeric(1),
        Alphanumeric(2),
        Byte(4),
        Kanji(8),
        ECI (7);
        private int value;
        private EncodingMode(int value) {
            this.value = value;
        }
    }

   public QRCode(int version,ECCLevel errorCorrection,int boxSize,int border) throws InvalidInput {
    Validate.CheckBoxSize(boxSize);
    //Validate.CheckMaskingPattern(maskPatten);
    Validate.CheckVersion(version);
    this.version=version;
    this.errorCorrection=errorCorrection.toString();
    this.boxSize=boxSize;
    this.border=border;
   // this.maskPattern=maskPatten;
   }

  public String TextToBinary(String data,EncodingMode ec){
        switch(ec){
            case Alphanumeric:
                return TextToBinaryAlphanumeric(data);
            case Numeric:
                return TextToBinaryNumeric(data);
            case Byte:
                return TextToBinaryBytes(data);
        }
        return "";
  }

  public static  String TextToBinaryAlphanumeric(String data){
        String codeText="";
        String[] array=data.split("(?<=\\G.{2})");
        return "";
  }

    public static  String TextToBinaryNumeric(String data){
        return "";
    }

    public static String TextToBinaryBytes(String data)
    {
        return " ";
    }



   public void CreateQRCode(String data){
        EncodingMode ec=GetEncodingFromPlaintext(data);
        String codedText=TextToBinary(data,ec);
   }



   public static EncodingMode GetEncodingFromPlaintext(String plaintext){
       EncodingMode result =EncodingMode.Default; // assume numeric
       if (plaintext.matches("[0-9]+") && plaintext.length() > 2) {
            result=EncodingMode.Numeric;
       }
       else if(plaintext.matches("[0-9A-Z\\s$%*+-./:]+") && plaintext.length() > 2){
          result=EncodingMode.Alphanumeric;
       }
       else if(plaintext.matches("^[\\x20-\\x7E\\xA0-\\xA3\\xA5\\xA7\\xA9-\\xB3\\xB5-\\xB7\\xB9-\\xBB\\xBF-\\xFF\\u20AC\\u0160\\u0161\\u017D\\u017E\\u0152\\u0153\\u0178]*$") && plaintext.length() > 2){
          result=EncodingMode.Byte;
       }
       return result;
   }
}

