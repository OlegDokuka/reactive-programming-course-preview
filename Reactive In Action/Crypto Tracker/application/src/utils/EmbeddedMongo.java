package utils;

import java.io.IOException;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class EmbeddedMongo {

	public static void run() throws IOException {
		MongodStarter starter = MongodStarter.getDefaultInstance();

		String bindIp = "localhost";
		int port = 27017;
		MongodConfig mongodConfig = MongodConfig.builder()
				.version(Version.Main.PRODUCTION)
				.net(new Net(bindIp, port, Network.localhostIsIPv6()))
				.build();

		MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
		mongodExecutable.start();

		Runtime.getRuntime()
		       .addShutdownHook(new Thread(mongodExecutable::stop));
	}
}
