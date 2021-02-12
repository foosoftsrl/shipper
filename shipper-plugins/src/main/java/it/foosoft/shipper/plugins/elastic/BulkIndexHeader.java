package it.foosoft.shipper.plugins.elastic;

public class BulkIndexHeader {
	static class Index {
		public final String _index;
		Index(String indexName) {
			_index = indexName;
		}
	}
	public BulkIndexHeader.Index index;
	public BulkIndexHeader(String indexName) {
		index = new Index(indexName);
	}
}