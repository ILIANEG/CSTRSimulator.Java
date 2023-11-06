import CHG4343_Design_Project_CustomExcpetions.InvalidNotationException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class ChemicalSpecies {
   private double molarMass;
   private String name;
   public ChemicalSpecies(double molarMass, String name) throws InvalidNotationException, NumericalException {
      if(name == null || name.isEmpty()) {
         throw new InvalidNotationException("Name of chemical specie must be specified.");
      }
      if(molarMass <= 0) {
         throw new NumericalException("Invalid molar mass value. Molar mass must be greater then 0.");
      }
      this.molarMass = molarMass;
      this.name = name;
   }
   public ChemicalSpecies(ChemicalSpecies source) {
      this.name = source.name;
      this.molarMass = source.molarMass;
   }
   public ChemicalSpecies clone() {
      return new ChemicalSpecies(this);
   }
   public double getMolarMass() { return this.molarMass; }
   public void setMolarMass(double molarMass) throws NumericalException {
      if(molarMass <= 0) {
         throw new NumericalException("Invalid molar mass value. Molar mass must be greater then 0.");
      }
      this.molarMass = molarMass;
   }
   public String getName() { return this.name; }
   public void setName(String name) throws InvalidNotationException {
      if(name == null || name.isEmpty()) {
         throw new InvalidNotationException("Name of chemical specie must be specified.");
      }
      this.name = name;
   }
   public boolean equals(Object comparator) {
      if(comparator == null || comparator.getClass() != this.getClass()) return false;
      return this.name.equals(((ChemicalSpecies) comparator).name) && this.molarMass == ((ChemicalSpecies) comparator).molarMass;
   }
}
