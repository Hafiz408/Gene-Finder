
/**
 * This program will help you find all the genes in a DNA strand
 * Also its CG ratio and length of the gene
 * Include edu.duke.libraries for certain library classes 
 * @author (Mohammed Hafiz Shahul Hameed) 
 * @version (1.6.20)
 */


import edu.duke.*;
public class geneFinder {
    public int findStopCodon(String dna,int startIndex,String stopCodon){
        int currIndex = dna.indexOf(stopCodon,startIndex);
        while(currIndex != -1){
            int diff = currIndex - startIndex;
            if(diff % 3 == 0){
                return currIndex;
            }
            else{
                currIndex = dna.indexOf(stopCodon,currIndex+1);
            }
        }
        return dna.length();
    }
    public String findGene(String dna,int start){
        int startIndex = dna.indexOf("ATG",start);
        if(startIndex == -1){            
            return "";
        }
        int taaIndex = findStopCodon(dna,startIndex,"TAA");
        int tagIndex = findStopCodon(dna,startIndex,"TAG");
        int tgaIndex = findStopCodon(dna,startIndex,"TGA");
        int minIndex = Math.min(Math.min(taaIndex,tagIndex),tgaIndex);
        if(minIndex == dna.length()){
            return "";
        }
        return dna.substring(startIndex,minIndex + 3);
    }
    public StorageResource findAllGenes(String dna){
        int startIndex = 0;
        StorageResource genes = new StorageResource();
        while(true){
            String currGene = findGene(dna,startIndex);
            if(currGene.isEmpty()){
                break;
            }
            //System.out.println(currGene);
            genes.add(currGene);
            startIndex = dna.indexOf(currGene,startIndex) + currGene.length();
        }
        return genes;
    }
    public float cgRatio(String gene){
        int cgCount = 0;
        int index = 0;
        while(true){
            index = gene.indexOf("C",index);
            if(index == -1){
                break;
            }
            cgCount++;
            index = index + 1;
        }
        index = 0;
        while(true){
            index = gene.indexOf("G",index);
            if(index == -1){
                break;
            }
            cgCount++;
            index = index + 1;
        }
        float cgRatio = (float)cgCount/gene.length();
        return cgRatio;
    }
    public int cgCount(StorageResource geneFile){
        int count = 0;
        for(String gene : geneFile.data()){
            float ratio = cgRatio(gene);
            if(ratio > 0.35){
                count++;
            }            
        }
        return count;
    }
    public int geneLength(StorageResource geneFile){
        int count = 0,maxLength = 0;
        for(String gene : geneFile.data()){
            int length = gene.length();
            if(maxLength < length){
                maxLength = length;
            }
            if(length > 60){
                count++;
            }
        }
        System.out.println("Max length of gene is " + maxLength);
        return count;
    }
    public int countCodon(String dna,String codon){
        int count = 0,startIndex = 0;
        while(true){
            int index = dna.indexOf(codon,startIndex);
            if(index == -1){
                break;
            }
            count++;
            startIndex = index + 3;
        }        
        return count;
    }
    public void run(){
        FileResource dnaFile = new FileResource();        
        String dna1 = dnaFile.asString();
        String dnaUpper1 = dna1.toUpperCase();
        StorageResource geneFile1 = new StorageResource();
        geneFile1 = findAllGenes(dnaUpper1);
        System.out.println("Number of genes is " + geneFile1.size());
        System.out.println("Number of genes longer than 60 is " + geneLength(geneFile1));
        System.out.println("Number of genes with CG ratio greater than 0.35 is " + cgCount(geneFile1));
        
        URLResource dnaURL = new URLResource("https://users.cs.duke.edu/~rodger/GRch38dnapart.fa");
        String dna2 = dnaURL.asString();
        String dnaUpper2 = dna2.toUpperCase();
        StorageResource geneFile2 = new StorageResource();
        geneFile2 = findAllGenes(dnaUpper2);
        System.out.println("Number of genes is " + geneFile2.size());
        System.out.println("Number of genes longer than 60 is " + geneLength(geneFile2));
        System.out.println("Number of CTG in dna is " + countCodon(dnaUpper2,"CTG"));
        System.out.println("Number of genes with CG ratio greater than 0.35 is " + cgCount(geneFile2));
    }
}

