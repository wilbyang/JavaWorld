package opensource;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;

public class EmbeddeNeo4j {
	private static final String DB_PATH = "target/neo4j-hello-db";
	static String greeting;
	// database
	GraphDatabaseService graphDb;
	Node firstNode;
	Node secondNode;
	Relationship relationship;
	
	private static enum RelTypes implements RelationshipType {
		KNOWS
	}
	
	public static void main(final String[] args) {
		EmbeddeNeo4j hello = new EmbeddeNeo4j();
		hello.createDb();
		hello.removeData();
		hello.shutDown();
		
		System.out.println(greeting);
	}
	
	void createDb() {
		clearDb();
		// start DB
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		registerShutdownHook(graphDb);
		
		// start Transaction
		Transaction tx = graphDb.beginTx();
		try {
			// adding data
			firstNode = graphDb.createNode();
			firstNode.setProperty("message", "Hello, ");
			secondNode = graphDb.createNode();
			secondNode.setProperty("message", "World!");
			
			relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
			relationship.setProperty("message", "brave Neo4j ");
			
			// reading data
			System.out.println(firstNode.getProperty("message"));
			System.out.println(relationship.getProperty("message"));
			System.out.println(secondNode.getProperty("message"));
			
			greeting = (String) firstNode.getProperty("message") + (String) relationship.getProperty("message") + (String) secondNode.getProperty("message");
			
			Iterator<Relationship> it = firstNode.getRelationships().iterator();
			while(it.hasNext()) {
				Relationship r = it.next();
				Node[] nodes = r.getNodes();
				System.out.println(nodes[0].getProperty("message") + " " + r.getProperty("message") + " " + nodes[1].getProperty("message"));
			}
			
			tx.success();
		} finally {
			tx.finish();
		}
		
	}
	
	private void clearDb() {
		try {
			FileUtils.deleteRecursively(new File(DB_PATH));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	void removeData() {
		Transaction tx = graphDb.beginTx();
		try {
			// removing data
			firstNode.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING).delete();
			firstNode.delete();
			secondNode.delete();
			
			tx.success();
		} finally {
			tx.finish();
		}
	}
	
	void shutDown() {
		System.out.println("\nShutting down database");
		// shutdown server
		graphDb.shutdown();
	}
	
	// shutdown hook
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}
}
