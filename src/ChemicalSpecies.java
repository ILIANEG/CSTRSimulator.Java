import CHG4343_Design_Project_CustomExcpetions.NumericalException;

/**
 * Chemical species class represents a chemical species that might be present in the system. Current implementation
 * includes species name and molar mass (molar mass is not utilized in the implementatin of the current project
 * but might be useful if system is further extended to non-isothermal systems).
 */
public class ChemicalSpecies {
   private double molarMass; // appropriate units
   private String name; // name of chemical species

   /**
    * ChemicalSpecies object constructor.
    * @param name Name of chemical species.
    * @param molarMass Molar mass of species in appropriate units (if 0 then molar mass is unknown)
    */
   public ChemicalSpecies(String name, double molarMass) {
      // Check if name is valid
      if(name == null || name.isEmpty()) {
         throw new IllegalArgumentException("Name of chemical specie must be specified.");
      }
      // Check for negative molar mass. 0 is reserved for unspecified molar masses.
      if(molarMass < 0) {
         throw new NumericalException("Invalid molar mass value. Molar mass must not be negative.");
      }
      this.molarMass = molarMass;
      this.name = name;
   }

   /**
    * Copy constructor.
    * @param source source object of type ChemicalSpecies
    */
   public ChemicalSpecies(ChemicalSpecies source) {
      this.name = source.name;
      this.molarMass = source.molarMass;
   }

   /**
    * Clone method.
    * @return copy of current object
    */
   public ChemicalSpecies clone() {
      // Call to copy constructor
      return new ChemicalSpecies(this);
   }

   /* Accessors & Mutators */

   /**
    * Molar mass accessor.
    * @return molar mass.
    */
   public double getMolarMass() { return this.molarMass; }

   /**
    * Molar mass mutator.
    * @param molarMass molar mass in appropriate units.
    */
   public void setMolarMass(double molarMass) {
      if(molarMass < 0) throw new NumericalException("Attempting to assign negative molar mass to ChemicalSpecies object");
      this.molarMass = molarMass;
   }

   /**
    * Name accessor.
    * @return name of chemical species.
    */
   public String getName() { return this.name; }

   /**
    * Name mutator.
    * @param name name of chemical species.
    */
   public boolean setName(String name) {
      if(name == null || name.isEmpty()) throw new IllegalArgumentException("Attempting to assign invalid name to ChemicalSpecies object");
      this.name = name;
      return true;
   }

   /**
    * Equals method.
    * @param comparator
    * @return true if comparator is equal to current object.
    */
   @Override
   public boolean equals(Object comparator) {
      if(comparator == null || comparator.getClass() != this.getClass()) return false;
      return this.name.equals(((ChemicalSpecies) comparator).name) && this.molarMass == ((ChemicalSpecies) comparator).molarMass;
   }

   /**
    * toString method.
    * @return name of the chemical species.
    */
   @Override
   public String toString() {
      return this.name;
   }
}
