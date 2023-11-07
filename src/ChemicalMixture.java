import CHG4343_Design_Project_CustomExcpetions.*;

public class ChemicalMixture {
    private ChemicalSpecies[] species;
    private double[] concentrations; // Concentration in mol/m^3
    public ChemicalMixture(ChemicalSpecies[] species, double[] concentrations) throws ArrayException {
        // Perform data validation
        ChemicalMixture.validateData(species, concentrations);
        // Initialize instance variables
        this.species = new ChemicalSpecies[species.length];
        this.concentrations = new double[concentrations.length];
        // Since array species and concentrations must be of the same size, both of them can be copied in the same loop.
        for(int i = 0; i < species.length; i++) {
            this.species[i] = species[i].clone();
            this.concentrations[i] = concentrations[i];
        }
    }
    // Constructor for an empty mixture (represents pure solvent)
    public ChemicalMixture() {
        this.species = new ChemicalSpecies[0];
        this.concentrations = new double[0];
    }
    // Copy constructor
    public ChemicalMixture(ChemicalMixture source) throws NullPointerException {
        if(source == null) throw new NullPointerException("Source object in ChemicalMixture copy constructor is null.");
        this.species = new ChemicalSpecies[source.species.length];
        this.concentrations = new double[source.concentrations.length];
        for(int i = 0; i < source.species.length; i++) {
            this.species[i] = source.species[i].clone();
            this.concentrations[i] = source.concentrations[i];
        }
    }
    // Clone method
    public ChemicalMixture clone() {
        return new ChemicalMixture(this);
    }
    /* Accessors and Mutators */
    public ChemicalSpecies[] getSpecies() {
        ChemicalSpecies[] tempSpecies = new ChemicalSpecies[this.species.length];
        for(int i = 0; i < this.species.length; i++) {
            tempSpecies[i] = this.species[i].clone();
        }
        return tempSpecies;
    }
    public double[] getConcentrations() {
        double[] tempConcentrations = new double[this.concentrations.length];
        for(int i = 0; i < this.concentrations.length; i++) {
            tempConcentrations[i] = this.concentrations[i];
        }
        return tempConcentrations;
    }

    /* Setters are not provided for this class since it is a data structure.
     * It should be able to store data and access it in a controlled manner.
     * Other classes should be able to mutate this class in an "invasive way" (by reference).
     * Method to manipulate this data structure are provided below */

    // Get concentration of particular specie. Null does not need to be checked.
    public double getConcentration(ChemicalSpecies species) {
        for(int i = 0; i < this.species.length; i++) {
            if(this.species[i].equals(species)) return this.concentrations[i];
        }
        return 0;
    }

    // Check if given specie is "announced" in the mixture (even if the specie has concentration = 0).
    public boolean containsSpecie(ChemicalSpecies specie) {
        for(int i = 0; i < this.species.length; i++) {
            if(this.species[i].equals(specie)) return true;
        }
        return false;
    }
    public void addSpecies(ChemicalSpecies species, double concentration) throws NullPointerException, NumericalException {
        if(species == null) throw new NullPointerException("Adding a null specie to ChemicalComposition object.");
        if(concentration < 0) throw new NumericalException("Assigning negative concentration in ChemicalComposition object.");
        ChemicalSpecies[] tmpSpecies = new ChemicalSpecies[this.species.length + 1];
        double[] tmpConcentrations = new double[this.concentrations.length + 1];
        for(int i = 0; i < this.species.length; i++) {
            tmpSpecies[i] = this.species[i].clone();
            tmpConcentrations[i] = this.concentrations[i];
        }
        tmpSpecies[this.species.length] = species.clone();
        tmpConcentrations[this.concentrations.length] = concentration;
        this.concentrations = tmpConcentrations;
        this.species = tmpSpecies;
    }

    public void addMultipleSpecies(ChemicalSpecies[] species, double[] concentrations) throws ArrayException {
        ChemicalMixture.validateData(species, concentrations);
        ChemicalSpecies[] tempSpecies = new ChemicalSpecies[this.species.length+species.length];
        double[] tempConcentrations = new double[this.concentrations.length+concentrations.length];
        int i = 0;
        for(; i < this.species.length; i++) {
            tempSpecies[i] = this.species[i].clone();
            tempConcentrations[i] = this.concentrations[i];
        }
        for(int j = 0; j < species.length; j++, i++) {
            tempSpecies[i] = species[j].clone();
            tempConcentrations[i] = concentrations[j];
        }
        this.species = tempSpecies;
        this.concentrations = tempConcentrations;
    }
    public boolean equals(Object comparator) {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        if(((ChemicalMixture) comparator).species.length != this.species.length);
        for(int i = 0; i < this.species.length; i++) {
            if(!this.species[i].equals(((ChemicalMixture) comparator).species[i])
                    || this.concentrations[i] != ((ChemicalMixture) comparator).concentrations[i]) return false;
        }
        return true;
    }
    public double calculateMolesSolute(ChemicalSpecies species, double volume) throws NumericalException {
        if(0 < volume) throw new NumericalException("Volume can not be negative when calculating moles of solute");
        return this.getConcentration(species) * volume;
    }
    public double calculateTotalMolesSolute(double volume) throws NumericalException {
        if(0 < volume) throw new NumericalException("Volume can not be negative when calculating moles of solute");
        double totalMolesSolute = 0;
        for(int i = 0; i < this.species.length; i++) {
            totalMolesSolute += this.concentrations[i] * volume;
        }
        return totalMolesSolute;
    }
    public double calculateMolarFraction(ChemicalSpecies species, double volume) throws NumericalException {
        return calculateMolesSolute(species, volume) / calculateTotalMolesSolute(volume);
    }
    // Helper (private class method) that validates data for the constructor or multiple species addition
    private static void validateData(ChemicalSpecies[] species, double[] concentrations) throws ArrayException {
        // Species array is null check
        if(species == null) {
            // Array exception with NullPointerException as a cause
            throw new ArrayException("List of chemical species was found to be null while " +
                    "initializing ChemicalComposition object.", new NullPointerException("Array species is null"));
        }
        // Concentrations array is null check
        if(concentrations == null) {
            // Array exception with NulPointerException as a cause
            throw new ArrayException("List of concentrations was found to be invalid while " +
                    "initializing ChemicalComposition object.", new NullPointerException("Array concentrations is null"));
        }
        // Species and concentrations arrays length check
        if(species.length != concentrations.length) {
            // Array exception with LengthMismatch
            throw new ArrayException("Chemical species array does not match the length of concentration array.",
                    new LengthMismatch("Related arrays' lengths mismatch.", species.length, concentrations.length));
        }
        // Checking all elements of both arrays (since length of both must be matching at this point)
        for(int i = 0; i < species.length; i++) {
            /* Array exception in species with cause being invalid data at index i.
            * Cause is further specified being NullPointerException */
            if(species[i] == null) throw new ArrayException("Specie at index "+ i+" is invalid.",
                    new InvalidArrayDataException(new NullPointerException("Species is null"), i));
            /* Array exception in concentrations with cause being invalid data at index i.
            * Cause is further specified being NumericalException.
            * Concentration = 0 is a valid concentration! */
            if(concentrations[i] < 0) throw new ArrayException("Concentration at index "+i+" is invalid.",
                    new InvalidArrayDataException(new NumericalException("Negative concentration."), i));
        }
    }
}
