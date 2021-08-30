package cryptator.solver.gentest;

public class CryptaPermutation extends CryptaHeapSolver{
    private int[] permutation;
    private int[] compteur;
    private int i=0;


    public CryptaPermutation(int[] permutation) {
        this.permutation = permutation;
        this.compteur=makeArray0(permutation.length);
    }

    @Override
    public boolean hasNext() {
        return i<permutation.length;
    }

    @Override
    public int[] next() {
        if (compteur[i] <i) {
            if(i%2==0) {
                //échanger A[ 0]et A[ i]
                int temp = permutation[0];
                permutation[0]= permutation[i];
                permutation[i]= temp;
            }
            else {
                //échanger A[ compteur[i]],A[i]
                int temp = permutation[compteur[i]];
                permutation[compteur[i]]= permutation[i];
                permutation[i]= temp;
            }
            //écrire A

            compteur[i] += 1; // on incrémente l'indice de boucle après avoir effectué une itération
            i=0; // on retourne au cas de base de la version récursive
        }
        else {
            // la boucle de niveau i est terminée, on peut donc réinitialiser l'indice et retourner au niveau supérieur
            compteur[i] =0;
            i += 1;

        }
        return permutation;
    }

    public void setPermutation(int[] permutation) {
        this.permutation = permutation;
    }

    public void setCompteur(int[] compteur) {
        this.compteur = compteur;
    }

    public void setI(int i) {
        this.i = i;
    }
}
