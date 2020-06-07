public class Validate {
   public static void CheckVersion(int version) throws InvalidInput {
     if(version<1 || version>40){
       throw new InvalidInput(String.format("Invalid version (was %s, expected 1 to 40)",version));
     }
   }

   public static void CheckBoxSize(int boxSize)throws InvalidInput{
       if(boxSize<=0){
           throw new InvalidInput(String.format("Invalid box size (was %s, expected larger than 0)",boxSize));
       }
   }

   public static void CheckMaskingPattern(String maskingPattern)throws InvalidInput{
       if(Integer.parseInt(maskingPattern)<0 || Integer.parseInt(maskingPattern)>7){
           throw new InvalidInput(String.format("Mask pattern should be in range(8) (got %s)",maskingPattern));
       }
   }
}
