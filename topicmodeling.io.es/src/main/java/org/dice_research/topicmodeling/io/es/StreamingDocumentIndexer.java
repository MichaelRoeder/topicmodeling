package org.dice_research.topicmodeling.io.es;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import org.apache.http.HttpHost;
import org.dice_research.topicmodeling.preprocessing.docconsumer.DocumentConsumer;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.elasticsearch.client.RestClient;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.util.BinaryData;
import co.elastic.clients.util.ContentType;

public class StreamingDocumentIndexer implements DocumentConsumer, AutoCloseable {

    private BulkIngester<Void> ingester;
    private RestClient restClient;
    private ElasticsearchTransport transport;
    private String indexName;
    private Function<Document, String> documentTransformion;

    public StreamingDocumentIndexer(RestClient restClient, ElasticsearchTransport transport,
            BulkIngester<Void> ingester, String indexName, Function<Document, String> documentTransformion) {
        super();
        this.ingester = ingester;
        this.restClient = restClient;
        this.transport = transport;
        this.indexName = indexName;
        this.documentTransformion = documentTransformion;
    }

    @Override
    public void consumeDocument(Document document) {
        String data = documentTransformion.apply(document);
        if (data != null) {
            BinaryData bdata = BinaryData.of(data.getBytes(StandardCharsets.UTF_8), ContentType.APPLICATION_JSON);
            ingester.add(op -> op.index(idx -> idx.index(indexName).document(bdata)));
        }
    }

    @Override
    public void close() throws Exception {
        ingester.close();
        transport.close();
        restClient.close();
    }

    public static StreamingDocumentIndexer create(String host, int port, String index,
            Function<Document, String> documentTransformion) {
        // Create the low-level client
        RestClient restClient = RestClient.builder(new HttpHost(host, port)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);

        return new StreamingDocumentIndexer(restClient, transport,
                BulkIngester.of(b -> b.client(client).maxOperations(100)), index, documentTransformion);
    }

//    public static StreamingDocumentIndexer create(String host, int port, String index,
//            Class<? extends DocumentProperty> propertyClass) {
//        // Create the low-level client
//        RestClient restClient = RestClient.builder(new HttpHost(host, port)).build();
//
//        // Create the transport with a Jackson mapper
//        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
//
//        // And create the API client
//        ElasticsearchClient client = new ElasticsearchClient(transport);
//
//        return new StreamingDocumentIndexer(restClient, transport,
//                BulkIngester.of(b -> b.client(client).maxOperations(100)), index,
//                new PropertyBasedDocumentTransformer(propertyClass));
//    }

}
