import CHG4343_Design_Project_CustomExcpetions.InvalidNotationException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class ChemicalSpecies {
   private double molarMass; // molar mass in kg/mol
   private String name; // Name of Specie (for example Water/H2O/Hydrogen Dioxide or other applicable identifier)
   // Constructor
   public ChemicalSpecies(double molarMass, String name) throws InvalidNotationException, NumericalException {
      // Check if name is valid
      if(name == null || name.isEmpty()) {
         throw new InvalidNotationException("Name of chemical specie must be specified.");
      }
      // Check for negative molar mass. 0 is reserved for unspecified molar masses.
      if(molarMass < 0) {
         throw new NumericalException("Invalid molar mass value. Molar mass must not be negative.");
      }
      this.molarMass = molarMass;
      this.name = name;
   }
   // Copy Constructor
   public ChemicalSpecies(ChemicalSpecies source) {
      this.name = source.name;
      this.molarMass = source.molarMass;
   }
   // Clone method
   public ChemicalSpecies clone() {
      return new ChemicalSpecies(this);
   }
   /* Accessors & Mutators */
   public double getMolarMass() { return this.molarMass; }
   public void setMolarMass(double molarMass) throws NumericalException {
      if(molarMass < 0) {
         throw new NumericalException("Invalid molar mass value. Molar mass must not be negative.");
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
