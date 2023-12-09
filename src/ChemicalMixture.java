import CHG4343_Design_Project_CustomExcpetions.*;
/**
 * ChemicalMixture class represents a chemical mixture that might be present in the system. It aggregates chemical species
 * objects and assigns concentrations at matching indexes. In itself ChemicalMixture is a data structure that stores data
 * on chemical species and their concentrations in the mixture. Temperature of mixture is not utilized in the project, however,
 * it might be useful for the purposes of extending simulation for non-isothermal systems
 */
public class ChemicalMixture {
    private ChemicalSpecies[] species; // Array of chemical species.
    private double[] concentrations; // Concentration in appropriate units.
    private double temperature; // temperature of mixture in K.

    /**
     * Constructor for ChemicalSpecies class.
     * @param species Array of species objects.
     * @param concentrations Array of concentrations in appropriate units.
     */
    public ChemicalMixture(ChemicalSpecies[] species, double[] concentrations) {
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
        * for non-isothermal reactor implementation if one implemented in the future. Constructor can be overloaded
        * if temperature must be assigned. */
        this.temperature = 273.15;
    }

    /**
     * Overloaded ChemicalMixture constructor with temperature.
     * @param species Array of species objects.
     * @param concentrations Array of concentrations with appropriate units.
     * @param absoluteTemperature Temperature of mixture in Kelvin.
     */
    public ChemicalMixture(ChemicalSpecies[] species, double[] concentrations, double absoluteTemperature) {
        // utilizing non-overloaded constructor
        this(species, concentrations);
        if(0 <= temperature) throw new NumericalException("Absolute temperature can not be 0 or negative");
        this.temperature = absoluteTemperature;
    }
    /**
     * Constructor for an empty mixture (represents pure solvent at default temperature).
     */
    public ChemicalMixture() {
        this.species = new ChemicalSpecies[0];
        this.concentrations = new double[0];
        this.temperature = 273.15;
    }

    /**
     * Copy constructor.
     * @param source ChemicalMixture object.
     */
    public ChemicalMixture(ChemicalMixture source) {
        // Null check
        if(source == null) throw new IllegalArgumentException("Source object in ChemicalMixture copy constructor is null.");
        this.species = new ChemicalSpecies[source.species.length];
        this.concentrations = new double[source.concentrations.length];
        // Arrays deep copy
        for(int i = 0; i < source.species.length; i++) {
            this.species[i] = source.species[i].clone();
            this.concentrations[i] = source.concentrations[i];
        }
        this.temperature = source.temperature;
    }

    /**
     * Clone method.
     * @return deep copy of ChemicalMixture object.
     */
    public ChemicalMixture clone() {
        return new ChemicalMixture(this);
    }

    /* Accessors and Mutators */

    /**
     * Species getter.
     * @return array of chemical species present in the mixture (deep copy)
     */
    public ChemicalSpecies[] getSpecies() {
        ChemicalSpecies[] tempSpecies = new ChemicalSpecies[this.species.length];
        for(int i = 0; i < this.species.length; i++) {
            tempSpecies[i] = this.species[i].clone();
        }
        return tempSpecies;
    }

    /**
     * Species setter.
     * @param species array of ChemicalSpecies objects
     */
    public void setSpecies(ChemicalSpecies[] species) {
        if(species == null) throw new IllegalArgumentException("Attempting to assign invalid array of species to ChemicalMixture Object");
        if(species.length != this.concentrations.length) throw new ArrayException("Attempting to assign array of species to ChemicalMixture Object" +
                "with the length that does not match array of concentrations in the ChemicalMixture Object");
        ChemicalSpecies[] tmpSpecies = new ChemicalSpecies[this.species.length];
        // Validate and deep copy the array
        for(int i = 0; i < this.species.length; i++) {
            if(species[i] == null) throw new InvalidArrayDataException("Invalid chemical species is encountered at index "
                    + i + " while assigning array species to ChemicalMixture object", i);
            tmpSpecies[i] = this.species[i].clone();
        }
        // Dumping reference
        this.species = tmpSpecies;
    }

    /**
     * Concentrations getter.
     * @return array of concentrations of all species in the mixture.
     */
    public double[] getConcentrations() {
        double[] tempConcentrations = new double[this.concentrations.length];
        for(int i = 0; i < this.concentrations.length; i++) {
            tempConcentrations[i] = this.concentrations[i];
        }
        return tempConcentrations;
    }

    /**
     * Concentrations setter.
     * @param concentrations concentrations of all species in the mixture in appropriate units.
     */
    public void setConcentrations(double[] concentrations) {
        if(concentrations == null )
            throw new IllegalArgumentException("Attempting to assign invalid array of concentrations to ChemicalMixture Object");
        if(concentrations.length != this.species.length)
            throw new ArrayException("Attempting to assign array of concentrations to ChemicalMixture Object" +
                    "with the length that does not match array of concentrations in the ChemicalMixture Object");
        double[] tmpConcentrations = new double[concentrations.length];
        // Validate and deep copy the array
        for(int i = 0; i < this.concentrations.length; i++) {
            if(concentrations[i] < 0) throw new InvalidArrayDataException("Invalid concentration is encountered at index "
                    + i + " while assigning array species to ChemicalMixture object", i);
            tmpConcentrations[i] = concentrations[i];
        }
        // Dumping reference
        this.concentrations = tmpConcentrations;
    }

    /**
     * Temperature getter.
     * @return temperature in K.
     */
    public double getTemperature() {
        return this.temperature;
    }

    /**
     * Temperature setter.
     * @param temperature temperature in K.
     */
    public void setTemperature(double temperature) {
        if(temperature <= 0) throw new NumericalException("Attamepting to assign negative or 0 absolute temperature to ChemicalMixture object");
        this.temperature = temperature;
    }

    /**
     * Computes number of species in the mixture.
     * @return number of species.
     */
    public int getNumberOfSpecies() {
        return this.species.length;
    }

    /**
     * Check if given species is "announced" in the mixture (even if the species has concentration = 0).
     * @param species ChemicalSpecie object
     * @return Whether species is announced in the mixture.
     */
    public boolean containsSpecies(ChemicalSpecies species) {
        if(this.getSpeciesIndex(species) == -1) return false;
        else return true;
    }

    /**
     * Return index of species.
     * @param species ChemicalSpecie object.
     * @return Index of a specie in the array, -1 if specie is not in the array.
     */
    public int getSpeciesIndex(ChemicalSpecies species) {
        for(int i = 0; i < this.species.length; i++) {
            if(this.species[i].equals(species)) return i;
        }
        return -1;
    }

    /**
     * Get concentration of particular species.
     * @param species ChemicalSpecies object.
     * @return concentration of specie in mixture, or 0 if specie is not found in the mixture
     */
    public double getConcentration(ChemicalSpecies species) {
        // Null does not need to be checked, if specie is null, 0 will be returned
        if(this.containsSpecies(species)) return this.concentrations[this.getSpeciesIndex(species)];
        else return 0;
    }

    /**
     * Add new species to the mixture.
     * @param species array of species.
     * @param concentration array of concentrations.
     */
    private void addNewSpecies(ChemicalSpecies species, double concentration) {
        // Checking species for null.
        if(species == null) throw new IllegalArgumentException("Encountered invalid species while adding a species" +
                "to ChemicalMixture");
        if(concentration < 0) throw new NumericalException("Encountered invalid concentration while adding a species" +
                "to ChemicalMixture");
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
     * Method sets concentration of species in the mixture. Add species if not already in the mixture.
     * @param species Species which concentration being altered.
     * @param concentration New concentration to be set for a specie.
     */
    public void setConcentration(ChemicalSpecies species, double  concentration) {
        if(concentration < 0) throw new NumericalException("Attempting to assign negative concentration to a species in ChemicalMixture object");
        if(this.containsSpecies(species)) this.concentrations[this.getSpeciesIndex(species)] = concentration;
        // addNewSpecies() does null check;
        else this.addNewSpecies(species, concentration);
    }

    /**
     * Adds species to the mixture only if the specie is not already in the mixture.
     * @param species species being added to the mixture.
     * @param concentration concentration of the species.
     */
    public void addSpecies(ChemicalSpecies species, double  concentration) {
        if(!this.containsSpecies(species)) this.setConcentration(species, concentration);
    }
    /**
     * Method that removes specie from the mixture.
     * @param species ChemicalSpecies object.
     * TODO: testing (not tested since unused).
     */
    public void removeSpecies(ChemicalSpecies species) {
        if(this.containsSpecies(species)) {
            // grab index of species to be removed
            int index = this.getSpeciesIndex(species);
            // Resize temporary arrays to be one smaller then original
            ChemicalSpecies[] tmpSpecies = new ChemicalSpecies[this.species.length - 1];
            double[] tmpConcentration = new double[this.concentrations.length - 1];
            // i tracks index in the original array, j tracks index in the new array
            for(int i = 0, j = 0; i < this.species.length; i++, j++) {
                // when index encountered, move index of new array 1 back
                if(i == index) j--;
                else {
                    tmpSpecies[j] = this.species[i].clone();
                    tmpConcentration[j] = this.concentrations[i];
                }
            }
            // dump references
            this.species = tmpSpecies;
            this.concentrations = tmpConcentration;
        }
    }

    /**
     * Equals method
     * @param comparator
     * @return true if objects are equal.
     */
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
     * @return String representation of the chemical mixture.
     */
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
     * Helper (private class method) that validates data for the constructor or multiple species addition method.
     * @param species Array of Species objects.
     * @param concentrations Array of concentration for each species in appropriate units.
     * @throws ArrayException If any arrays or element or arrays are invalid.
     */
    private static void validateData(ChemicalSpecies[] species, double[] concentrations) {
        // Species array null check
        if(species == null) {
            throw new IllegalArgumentException("List of chemical species was found to be null while " +
                    "initializing ChemicalComposition object.");
        }
        // Concentrations array null check
        if(concentrations == null) {
            // Array exception with NulPointerException as a cause
            throw new IllegalArgumentException("List of concentrations was found to be invalid while " +
                    "initializing ChemicalComposition object.");
        }
        if(species.length != concentrations.length) {
            // Throw exception if length mismatch is encountered.
            throw new ArrayException("Chemical species array does not match the length of concentrations array.");
        }
        // Checking all elements of both arrays (since length of both must be matching at this point).
        for(int i = 0; i < species.length; i++) {
            // Throw exception if species in the array is null.
            if(species[i] == null) throw new InvalidArrayDataException("Concentration at index "+i+" is invalid.", i);
            // Throw exception if a negative concentration is encountered.
            if(concentrations[i] < 0) throw new InvalidArrayDataException("Concentration at index "+i+" is invalid.", i);
        }
    }
}
