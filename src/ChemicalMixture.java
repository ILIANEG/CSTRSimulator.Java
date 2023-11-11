import CHG4343_Design_Project_CustomExcpetions.*;

public class ChemicalMixture {
    private ChemicalSpecies[] species;
    private double[] concentrations; // Concentration in mol/m^3.
    private double temperature; // temperature of mixture in K.

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
        /* Setting default temperature since it is not relevant for the current project, instead it is relevant
        * for non-isothermal reactor implementation if one implemented in the future. */
        this.temperature = 273.15;
    }
    public ChemicalMixture(ChemicalSpecies[] species, double[] concentrations, double temperature) throws ArrayException {
        this(species, concentrations);
        /* Setting default temperature since it is not relevant for the current project, instead it is relevant
         * for non-isothermal reactor implementation if one implemented in the future. */
        this.temperature = 273.15;
    }
    /**
     * Constructor for an empty mixture (represents pure solvent).
     */
    public ChemicalMixture() {
        this.species = new ChemicalSpecies[0];
        this.concentrations = new double[0];
        this.temperature = 273.15;
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
    public double getTemperature() {
        return this.temperature;
    }

    /**
     * set temperature of the Chemical Mixture.
     * @param temperature Temperature, units: K.
     * @throws NumericalException If Absolute temperature is less then or equal to zero.
     */
    public void setTemperature(double temperature) throws NumericalException {
        if(temperature <= 0) throw new NumericalException("Absolute temperature can not be negative or zero");
        this.temperature = temperature;
    }

    /* Some setters are not provided for this class since it is a data structure.
     * It should be able to store data and access it in a controlled manner.
     * Other classes should be able to mutate fields of this class in an "invasive way" (by reference).
     * Method to manipulate this data structure are provided below. */

    public int getNumberOfSpecies() {
        return this.species.length;
    }
    /**
     * Get concentration of particular specie
     * @param species ChemicalSpecies object.
     * @return concentration of specie in mixture, or 0 if specie is not found in the mixture
     */
    public double getConcentration(ChemicalSpecies species) {
        // Null does not need to be checked, is specie is null, 0 will be returned
        if(this.containsSpecies(species)) return this.concentrations[this.getSpeciesIndex(species)];
        return 0;
    }

    /**
     * Method sets concentration of specie in the mixture. Add species if the specie is not already in the mixture.
     * @param species Specie which concentration being altered.
     * @param concentration New concentration to be set for a specie.
     * @throws NumericalException If new concentration is negative.
     */
    public void setConcentration(ChemicalSpecies species, double  concentration) throws NumericalException, NullPointerException {
        if(concentration < 0) throw new NumericalException("Attempting to assign negative concentration to a species in ChemicalMixture object");
        // Null does not need to be checked, is specie is null, 0 will be returned
        if(this.containsSpecies(species)) this.concentrations[this.getSpeciesIndex(species)] = concentration;
        else this.addNewSpecies(species, concentration);
    }

    /**
     *
     * @param species Specie which concentration being altered.
     * @param concentration New concentration to be set for a specie.
     * @throws NumericalException If any concentration is negative
     * @throws NullPointerException
     */
    public void addSpecies(ChemicalSpecies species, double  concentration) throws NumericalException {
        if(!this.containsSpecies(species)) this.setConcentration(species, concentration);
    }
    /**
     * Method that removes specie from the mixture.
     * @param species ChemicalSpecies object.
     */
    public void removeSpecies(ChemicalSpecies species) {
        if(this.containsSpecies(species)) {
            int index = this.getSpeciesIndex(species);
            ChemicalSpecies[] tmpSpecies = new ChemicalSpecies[this.species.length - 1];
            double[] tmpConcentration = new double[this.concentrations.length - 1];
            for(int i = 0, j = 0; i < this.species.length; i++, j++) {
                if(i == index) j--;
                else {
                    tmpSpecies[j] = this.species[i].clone();
                    tmpConcentration[j] = this.concentrations[i];
                }
            }
            this.species = tmpSpecies;
            this.concentrations = tmpConcentration;
        }
    }
    /**
     * Check if given specie is "announced" in the mixture (even if the specie has concentration = 0).
     * @param specie ChemicalSpecie object
     * @return Whether specie is announced in the mixture.
     */
    public boolean containsSpecies(ChemicalSpecies specie) {
        if(this.getSpeciesIndex(specie) == -1) return false;
        return true;
    }
    /**
     * addSpecies() method uses principal of List data structure to add new element to the mixture.
     * @param species ChemicalSpecie object to be added to the mixture.
     * @param concentration Concentration of specie in the mixture, units: mol/m^3.
     * @throws NullPointerException if ChemicalSpecies object is null.
     * @throws NumericalException if concentration is negative.
     */
    private void addNewSpecies(ChemicalSpecies species, double concentration) throws NullPointerException, NumericalException {
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
    // TODO: add check for same name of the species.
    /**
     * addMultipleSpecies() operates similarly to addSpecies() method, however adds multiple species at the same time.
     * @param species array of species objects to be added.
     * @param concentrations array of concentrations, units: mol/m^3.
     * @throws ArrayException if array or any of its elements are invalid.
     */
    public void addMultipleSpecies(ChemicalSpecies[] species, double[] concentrations) throws ArrayException, NumericalException {
        ChemicalMixture.validateData(species, concentrations);
        for(int i = 0; i < species.length; i++) this.setConcentration(species[i], concentrations[i]);
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
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < this.species.length; i++) {
            output.append("[").append(this.species[i].toString()).append("] = ").append(this.concentrations[i]);
            if(i < this.species.length - 1) output.append("; ");
        }
        return output.toString();
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
     * Helper method that finds index of a chemical specie.
     * @param species ChemicalSpecie object.
     * @return Index of a specie in the array, or -1 if specie is not in the array.
     */
    public int getSpeciesIndex(ChemicalSpecies species) {
        for(int i = 0; i < this.species.length; i++) {
            if(this.species[i].equals(species)) return i;
        }
        return -1;
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
