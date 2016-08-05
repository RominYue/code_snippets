package testStandfordNLP;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import edu.stanford.nlp.hcoref.data.CorefChain;
import edu.stanford.nlp.naturalli.Polarity;
import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Quadruple;

public class TestMain {

    public static void main(String[] args) {
        String text = "add your text here! It can contain multiple sentences.";
        //generate document object
        Document doc = new Document(text);

        //split to sentences
        List<Sentence> sentences = doc.sentences();

        //get words for each sentences
        for(Sentence sent: sentences){
            System.out.println("Sentences are:");

            //tokenize
            List<String> words = sent.words();
            System.out.println("tokens are:");
            print_list(words);

            //lemma
            List<String> lemmas = sent.lemmas();
            System.out.println("lemmas are:");
            print_list(lemmas);

            //pos taggger
            List<String> postags = sent.posTags();
            System.out.println("POS taggers are:");
            print_list(postags);

            //ner
            List<String> nertags = sent.nerTags();
            System.out.println("Ner taggers are:");
            print_list(nertags);

            //constituency parser
            Tree parseTree = sent.parse();
            System.out.println("Constituency parse result:");
            System.out.println(parseTree);

            //dependcy parser
            List<Optional<Integer>> deps = sent.governors();
            System.out.println("Dependcy parse result:");
            print_deps(deps);

            //coreferences  document level, exausted
            //Map<Integer,CorefChain> coreferences = sent.coref();
            //System.out.println("coreference chain is:");
            //System.out.println(coreferences);

            //logic notion of polarity
            List<Polarity> logicNotions = sent.natlogPolarities();
            System.out.println("Logic Notion of Polarity are:");
            System.out.println(logicNotions);

            //openie
            //Collection<Quadruple<String,String,String,Double>> relations = sent.openie();
            //System.out.println("Open IE results:");
            //System.out.println(relations);
        }

    }

    public static void print_list(List<String> s) {
        for(String tmp: s){
            System.out.println(tmp);
        }
        System.out.println("");
    }

    public static void print_deps(List<Optional<Integer>> list) {
        for(Optional<Integer> ops: list){
            if(ops.isPresent()){
                System.out.println(ops.get());
            }
        }
    }
}
