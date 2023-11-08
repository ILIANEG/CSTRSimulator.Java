import CHG4343_Design_Project_CustomExcpetions.*;

public class ChemicalMixture {
    private ChemicalSpecies[] species;
    private double[] concentrations; // Concentration in mol/m^3.

    /**
     * Constructor for ChemicalSpecies class.
     * @param species Array of species objects.
     * @param concentrations Array fo concentrations, units: mol/m^3
     * @throws ArrayException If array species and/or concentrations are invalid.
     */
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

    /**
     * Constructor for an empty mixture (represents pure solvent).
     */
    public ChemicalMixture() {
        this.species = new ChemicalSpecies[0];
        this.concentrations = new double[0];
    }
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
     * Method to manipulate this data structure are provided below. */

    /**
     * Get concentration of particular specie
     * @param species ChemicalSpecies object.
     * @return concentration of specie in mixture, or 0 if specie is not found in the mixture
     */
    public double getConcentration(ChemicalSpecies species) {
        // Null does not need to be checked, is specie is null, 0 will be returned
        for(int i = 0; i < this.species.length; i++) {
            if(this.species[i].equals(species)) return this.concentrations[i];
        }
        return 0;
    }

    /**
     * Check if given specie is "announced" in the mixture (even if the specie has concentration = 0).
     * @param specie ChemicalSpecie object
     * @return Whether specie is announced in the mixture.
     */
    public boolean containsSpecie(ChemicalSpecies specie) {
        for(int i = 0; i < this.species.length; i++) {
            if(this.species[i].equals(specie)) return true;
        }
        return false;
    }

    /**
     * addSpecies() method uses principal of List data structure to add new element to the mixture.
     * @param species ChemicalSpecie object to be added to the mixture.
     * @param concentration Concentration of specie in the mixture, units: mol/m^3.
     * @throws NullPointerException if ChemicalSpecies object is null.
     * @throws NumericalException if concentration is negative.
     */
    public void addSpecies(ChemicalSpecies species, double concentration) throws NullPointerException, NumericalException {
        // Checking species for null.
        if(species == null) throw new NullPointerException("Adding a null specie to ChemicalComposition object.");
        // Checking for negative concentration.
        if(concentration < 0) throw new NumericalException("Assigning negative concentration in ChemicalComposition object.");
        // Initializing temporary species and temporary concentrations array with size 1 larger than original arrays.
        ChemicalSpecies[] tmpSpecies = new ChemicalSpecies[this.species.length + 1];
        double[] tmpConcentrations = new double[this.concentrations.length + 1];
        // Copy over the original species and concentrations array.
        for(int i = 0; i < this.species.length; i++) {
            // Copying referenced. It is safe within given context, since reference is not coming from the outside of the class.
            tmpSpecies[i] = this.species[i];
            tmpConcentrations[i] = this.concentrations[i];
        }
        // New element is added (species id deep copied this time, since reference is coming from the outside of the class).
        tmpSpecies[this.species.length] = species.clone();
        tmpConcentrations[this.concentrations.length] = concentration;
        // Temporary arrays references are dumped into the instance variables.
        this.concentrations = tmpConcentrations;
        this.species = tmpSpecies;
    }

    /**
     * addMultipleSpecies() operates similarly to addSpecies() method, however adds multiple species at the same time.
     * @param species array of species objects to be added.
     * @param concentrations array of concentrations, units: mol/m^3.
     * @throws ArrayException if array or any of its elements are invalid.
     */
    public void addMultipleSpecies(ChemicalSpecies[] species, double[] concentrations) throws ArrayException {
        // Using validate method on arrays that were passed.
        ChemicalMixture.validateData(species, concentrations);
        // Initializing temporary arrays of size to accommodate both original arrays and new arrays
        ChemicalSpecies[] tempSpecies = new ChemicalSpecies[this.species.length+species.length];
        double[] tempConcentrations = new double[this.concentrations.length+concentrations.length];
        // Copying original arrays into temporary arrays
        for(int i = 0; i < this.species.length; i++) {
            // Yet again, copying by reference is safe since elements are coming from within the class.
            tempSpecies[i] = this.species[i];
            tempConcentrations[i] = this.concentrations[i];
        }
        // Copying new elements. This time species are deep-copied, since coming from outside the class.
        for(int i = 0; i < species.length; i++) {
            tempSpecies[i + this.species.length] = species[i].clone();
            tempConcentrations[i + this.concentrations.length] = concentrations[i];
        }
        // Temporary arrays references are dumped into the instance variables.
        this.species = tempSpecies;
        this.concentrations = tempConcentrations;
    }
    @ Override
    public boolean equals(Object comparator) {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        if(((ChemicalMixture) comparator).species.length != this.species.length) return false;
        for(int i = 0; i < this.species.length; i++) {
            if(!this.species[i].equals(((ChemicalMixture) comparator).species[i])
                    || this.concentrations[i] != ((ChemicalMixture) comparator).concentrations[i]) return false;
        }
        return true;
    }

    /**
     * Method calculateMolesSolute() calculate moles of solute in the mixture.
     * @param species ChemicalSpecie object.
     * @param volume Volume of mixture, units: m^3.
     * @return Moles of solute in the mixture.
     */
    public double calculateMolesSolute(ChemicalSpecies species, double volume) {
        if(volume < 0) return 0;
        return this.getConcentration(species) * volume;
    }
    /**
     * Helper (private class method) that validates data for the constructor or multiple species addition method.
     * @param species Array of Specie objects.
     * @param concentrations Array of concentration for each specie, units: mol/m^3.
     * @throws ArrayException If any arrays or element or arrays are invalid.
     */
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
