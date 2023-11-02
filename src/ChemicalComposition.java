import CustomExcpetions.*;

public class ChemicalComposition {
    private ChemicalSpecies[] species;
    private double[] concentrations; // Concentration in mole/m^3
    public ChemicalComposition(ChemicalSpecies[] species, double[] concentrations) throws ArrayException {
        if(species == null) {
            throw new ArrayException("List of chemical species was found to be null while " +
                    "initializing ChemicalComposition object.", new NullPointerException("Array species is null"));
        }
        if(concentrations == null || concentrations.length == 0) {
            throw new ArrayException("List of concentrations was found to be invalid while " +
                    "initializing ChemicalComposition object.", new NullPointerException("Array concentrations is null"));
        }
        if(species.length != concentrations.length) {
            throw new ArrayException("Chemical species array does not match the length of concentration array.",
                    new LengthMismatch("Related arrays' lengths mismatch.", species.length, concentrations.length));
        }
        ChemicalSpecies[] tempSpecies = new ChemicalSpecies[species.length];
        double[] tempConcentration = new double[concentrations.length];
        for(int i = 0; i < species.length; i++) {
            if(species[i] == null) throw new ArrayException("Specie at index "+ i+" is invalid.", new InvalidArrayDataException(i));
            tempSpecies[i] = species[i];
            if(concentrations[i] < 0) throw new ArrayException("Concentration at index "+i+" is invalid.",
                    new InvalidArrayDataException(new NumericalException("Negative concentration."), i));
            tempConcentration[i] = concentrations[i];
        }
        this.species = tempSpecies;
        this.concentrations = tempConcentration;
    }
    public ChemicalComposition(ChemicalComposition source) throws NullPointerException {
        if(source == null) throw new NullPointerException("Source object in ChemicalComposition copy constructor is null.");
        this.species = new ChemicalSpecies[source.species.length];
        this.concentrations = new double[source.concentrations.length];
        for(int i = 0; i < source.species.length; i++) {
            this.species[i] = source.species[i].clone();
            this.concentrations[i] = source.concentrations[i];
        }
    }
    public ChemicalComposition clone() {
        return new ChemicalComposition(this);
    }
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
    public double getConcentration(ChemicalSpecies specie) {
        for(int i = 0; i < this.species.length; i++) {
            if(specie.equals(this.species[i])) return concentrations[i];
        }
        return 0;
    }
    public double getConcentration(String specieName) {
        for(int i = 0; i < this.species.length; i++) {
            if(specieName.equals(this.species[i].getName())) return this.concentrations[i];
        }
        return 0;
    }
    public boolean containsSpecie(ChemicalSpecies specie) {
        for(int i = 0; i < this.species.length; i++) {
            if(this.species[i].equals(specie)) return true;
        }
        return false;
    }
    public boolean containsSpecie(String specieName) {
        for(int i = 0; i < this.species.length; i++) {
            if(this.species[i].getName().equals(specieName)) return true;
        }
        return false;
    }
    public void addSpecie(ChemicalSpecies specie, double concentration) throws NullPointerException, NumericalException {
        if(specie == null) throw new NullPointerException("Adding a null specie to ChemicalComposition object.");
        if(concentration < 0) throw new NumericalException("Assigning negative concentration in ChemicalComposition object.");
        ChemicalSpecies[] species = new ChemicalSpecies[this.species.length + 1];
        double[] concentrations = new double[this.concentrations.length + 1];
        for(int i = 0; i < this.species.length; i++) {
            species[i] = this.species[i].clone();
            concentrations[i] = this.concentrations[i];
        }
        species[this.species.length] = specie.clone();
        concentrations[this.concentrations.length] = concentration;
        this.concentrations = concentrations;
        this.species = species;
    }
}
