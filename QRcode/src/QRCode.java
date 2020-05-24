public class QRCode {
   public int version;
   public int errorCorrection;
   public int boxSize;
   public int border;
   public String maskPattern;

   public QRCode(int version,int errorCorrection,int boxsize,int border,String maskPatten) throws InvalidInput {
    main.CheckBoxSize(boxSize);
    main.CheckMaskingPattern(maskPatten);
    main.CheckVersion(version);
    this.version=version;
    this.errorCorrection=errorCorrection;
    this.boxSize=boxsize;
    this.border=border;
    this.maskPattern=maskPatten;
   }

   public void addData(String data){

   }
}
