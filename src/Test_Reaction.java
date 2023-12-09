public class Test_Reaction {
    public static void main(String[] args)
    {
        ChemicalSpecies[] reactants = new ChemicalSpecies[] { new ChemicalSpecies("A",0)};
        ChemicalSpecies[] products = new ChemicalSpecies[] { new ChemicalSpecies("B",0)};
        double[] reactantsStoichiometry= new double[] {-1};
        double[] productsStoichiometry=new double[] {1};
        double k=0.2;
        ElementaryConstantKReaction test =new ElementaryConstantKReaction(reactants,products,reactantsStoichiometry,productsStoichiometry,k);
        ChemicalSpecies[] passm =new ChemicalSpecies[] {reactants[0],products[0]};
        double[] concentration = new double[] {0.04,0.16};
        ChemicalMixture mixture=new ChemicalMixture(passm,concentration);
        System.out.println("ReactionRate:\t"+test.calculateReactionRate(mixture));
    }
}
