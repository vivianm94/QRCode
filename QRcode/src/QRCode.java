import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Dictionary;
import java.util.Hashtable;

public class QRCode {
    private static char[] alphanumEncTable = { ' ', '$', '%', '*', '+', '-', '.', '/', ':' };
    private static Dictionary alphanumEncDict = CreatealphanumEncDict();
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

  public String TextToBinary(String data,EncodingMode ec) throws InvalidInput{
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

  public static String DectoBin(int num,int padLeft){
        if(Integer.toBinaryString(num).length()==padLeft){
            return Integer.toBinaryString(num);
        }
        else{
            String binnum=Integer.toBinaryString(num).toString();
            StringBuilder sb = new StringBuilder();
            for (int toPrepend=padLeft-binnum.length(); toPrepend>0; toPrepend--) {
                sb.append('0');
            }
            sb.append(binnum);
           return sb.toString();
        }
  }

  public static  String TextToBinaryAlphanumeric(String data){
        String codeText="";
        String[] array=data.split("(?<=\\G.{2})");
        for(int j=0;j<array.length;j++){
            char[]arraySplit=array[j].toCharArray();
            if(arraySplit.length>=2) {
                //int dec=alphanumEncTable[arraySplit[0]]*45+alphanumEncTable[arraySplit[1]];
                int dec=(int)alphanumEncDict.get(Character.toString(arraySplit[0]))*45+(int)alphanumEncDict.get(Character.toString(arraySplit[1]));
                codeText=codeText+DectoBin(dec,11);
            }
            else if(arraySplit.length<=2){
                int dec=(int)alphanumEncDict.get(Character.toString(arraySplit[0]));
                codeText=codeText+DectoBin(dec,6);
            }
        }
        return codeText;
  }

    public static  String TextToBinaryNumeric(String data){
        String codeText="";
        String[]array=data.split("(?<=\\G.{3})");
        for(int k=0;k<array.length;k++){
            if(array[k].length()>=3){
               int dec=Integer.parseInt(array[k]);
               codeText=codeText+DectoBin(dec,10);
            }
            else if(array[k].length()==2){
                int dec=Integer.parseInt(array[k]);
                codeText=codeText+DectoBin(dec,7);
            }
            else if(array[k].length()==1){
                int dec=Integer.parseInt(array[k]);
                codeText=codeText+DectoBin(dec,4);
            }
        }
        return codeText;
    }

    public static boolean IsValidISO(String data){
        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        if (enc.canEncode(data))
        {
            return true;
        }
        else{
            return false;
        }
    }

    public static String TextToBinaryBytes(String data) throws InvalidInput
    {
      String codeText="";
      if(IsValidISO(data)){
          byte[] codebyte = data.getBytes(StandardCharsets.ISO_8859_1);
          for(int j=0;j<codebyte.length;j++){
              codeText=codeText+DectoBin(codebyte[j],8);
          }
      }
      else {
          throw new InvalidInput(String.format("Invalid Input Data As per ISO-8859-1"));
      }
        return codeText;
    }

    public static Dictionary<String,Integer> CreatealphanumEncDict(){
        Dictionary localAlphanumEncDict = new Hashtable();
        int i;
        for (i = 0; i < 10; i++) {
            localAlphanumEncDict.put(Integer.toString(i),i);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            //localAlphanumEncDict.put(i,c);
            localAlphanumEncDict.put(Character.toString(c),i);
            i++;
        }
        for(int k=0;k<alphanumEncTable.length;k++){
           // localAlphanumEncDict.put(i,alphanumEncTable[k]);
            localAlphanumEncDict.put(Character.toString(alphanumEncTable[k]),i);
            i++;
        }
        return localAlphanumEncDict;
    }


    public static void CheckDataQRVersion(String data, int version,EncodingMode ec) throws InvalidInput{
      if(version>=1||version<=9){
        if(ec==EncodingMode.Numeric){
          if(!(data.length() <=10)){
              throw new InvalidInput(String.format("Input data is greater.Please Change the mode Or version of QR"));
          }
        }
        else if(ec==EncodingMode.Alphanumeric){
            if(!(data.length()<=9)){
                throw new InvalidInput(String.format("Input data is greater.Please Change the mode Or version of QR"));
            }
        }
        else if(ec==EncodingMode.Byte){
            if(!(data.length()<=8)){
                throw new InvalidInput(String.format("Input data is greater.Please Change the mode Or version of QR"));
            }
        }
      }
      else if(version>=10||version<=26){

          if(ec==EncodingMode.Numeric){
              if(!(data.length() <=12)){
                  throw new InvalidInput(String.format("Input data is greater.Please Change the mode Or version of QR"));
              }
          }
          else if(ec==EncodingMode.Alphanumeric){
              if(!(data.length()<=11)){
                  throw new InvalidInput(String.format("Input data is greater.Please Change the mode Or version of QR"));
              }
          }
          else if(ec==EncodingMode.Byte){
              if(!(data.length()<=16)){
                  throw new InvalidInput(String.format("Input data is greater.Please Change the mode Or version of QR"));
              }
          }

      }
      else if(version>=27||version<=40){
          if(ec==EncodingMode.Numeric){
              if(!(data.length() <=14)){
                  throw new InvalidInput(String.format("Input data is greater.Please Change the mode Or version of QR"));
              }
          }
          else if(ec==EncodingMode.Alphanumeric){
              if(!(data.length()<=13)){
                  throw new InvalidInput(String.format("Input data is greater.Please Change the mode Or version of QR"));
              }
          }
          else if(ec==EncodingMode.Byte){
              if(!(data.length()<=16)){
                  throw new InvalidInput(String.format("Input data is greater.Please Change the mode Or version of QR"));
              }
          }
      }
    }

    public static int GetCountIndicatorLength(int version,EncodingMode encMode){
        if (version < 10)
        {
            if (encMode == EncodingMode.Numeric)
                return 10;
            else if (encMode == EncodingMode.Alphanumeric)
                return 9;
            else
                return 8;
        }
        else if (version < 27)
        {
            if (encMode == EncodingMode.Numeric)
                return 12;
            else if (encMode == EncodingMode.Alphanumeric)
                return 11;
            else if (encMode == EncodingMode.Byte)
                return 16;
            else
                return 10;
        }
        else
        {
            if (encMode == EncodingMode.Numeric)
                return 14;
            else if (encMode == EncodingMode.Alphanumeric)
                return 13;
            else if (encMode == EncodingMode.Byte)
                return 16;
            else
                return 12;
        }
    }

   public void CreateQRCode(String data) throws InvalidInput{
        EncodingMode ec=GetEncodingFromPlaintext(data);
        String codedText=TextToBinary(data,ec);
        CheckDataQRVersion(data,version,ec);
        String modeIndicator=DectoBin(ec.value,4);
        String countIndicator=DectoBin(data.length(),GetCountIndicatorLength(version,ec));
        String bitString=modeIndicator+countIndicator+codedText;
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

