import CHG4343_Design_Project_CustomExcpetions.InvalidNotationException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class ChemicalSpecies {
   private double molarMass; // molar mass in kg/mol
   private String name;
   /**
    * ChemicalSpecies object constructor.
    * @param name Name of chemical species.
    * @param molarMass Molar mass of species (0 is unknown)
    * @throws InvalidNotationException If specie name is invalid (null or empty).
    * @throws NumericalException if molar mass is negative.
    */
   public ChemicalSpecies(String name, double molarMass) throws InvalidNotationException, NumericalException {
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
   public ChemicalSpecies(ChemicalSpecies source) {
      this.name = source.name;
      this.molarMass = source.molarMass;
   }
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
   public String toString() {
      return this.name;
   }
}
