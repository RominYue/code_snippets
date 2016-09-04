package edu.ruc.ir;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.DocumentAnalysisRequest;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.AnalysisResponseBase;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.AnalysisPhase;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.TokenInfo;
import org.apache.solr.client.solrj.response.DocumentAnalysisResponse;
import org.apache.solr.client.solrj.response.DocumentAnalysisResponse.DocumentAnalysis;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Ming on 16/9/1.
 */
public class TestSolrAPI {
    private static final String solrUrl = "";
    public static void main(String[] args) throws Exception {
        SolrClient solrClient = new HttpSolrClient(solrUrl);
        SolrQuery query = new SolrQuery();
        query.setQuery("id:clueweb09-en0001-34-28168");
        query.setRequestHandler("/select");
        query.set("fl","id,docfreq(body,forums),$t,idf(url_text,forums)");
        query.set("t","termfreq(body,forums)");
        QueryResponse response = solrClient.query(query);
        SolrDocumentList docs = response.getResults();
        for(SolrDocument doc: docs){
            for(String key: doc.keySet()){
                System.out.printf("%s %s\n",key, doc.get(key).toString());
            }
        }

        String sentence = "Bring Out Your Soups and Stews! - Harvest Forum - GardenWeb";
        List<String> ret = new ArrayList<>();
        ret = getAnalysis(sentence, solrClient);
        System.out.println(ret);

        //System.out.println(getDocAnalysis(solrClient));
    }

    public static List<String> getAnalysis(String sentence, SolrClient solrClient) throws Exception{
        FieldAnalysisRequest request = new FieldAnalysisRequest("/analysis/field");
        request.addFieldType("text_general");
        request.setFieldValue(sentence);
        request.setQuery("");

        FieldAnalysisResponse response = null;
        response = request.process(solrClient);

        List<String> results = new ArrayList<>();
        Iterator<AnalysisPhase> it = response.getFieldTypeAnalysis("text_general").getIndexPhases().iterator();
        AnalysisPhase pharse = null;
        while (it.hasNext()){
            pharse = (AnalysisPhase)it.next();
        }
        List<TokenInfo> list = pharse.getTokens();
        for(TokenInfo info: list){
            results.add(info.getText());
        }
        return results;
    }

    public static List<String> getDocAnalysis(SolrClient solrClient) throws Exception{
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", "clueweb09-en0001-34-28168");
        doc.addField("body", "//<![CDATA[ document.write('<scr'+");

        DocumentAnalysisRequest request = new DocumentAnalysisRequest("/analysis/document");
        request.addDocument((doc));
        request.setQuery("Bring Out Your Soups and Stews! - Harvest Forum - GardenWeb");

        DocumentAnalysisResponse response = null;
        response = request.process(solrClient);
        /*
        DocumentAnalysisResponse.FieldAnalysis fa = response.getDocumentAnalysis("clueweb09-en0001-34-28168").getFieldAnalysis("body");
        Iterator<AnalysisPhase> it = fa.getIndexPhases((String) doc.getFieldValue("body")).iterator();
        Iterator<AnalysisPhase> it = fa.getQueryPhases().iterator();
        AnalysisPhase pharse = null;

        List<String> results = new ArrayList<>();
        while (it.hasNext()){
            pharse = (AnalysisPhase)it.next();
        }
        List<TokenInfo> list = pharse.getTokens();
        for(TokenInfo info: list){
            results.add(info.getText());
        }*/
        List<String> results = null;
        return results;
    }

}
