package wrzlbrmft.ppcrypt;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Main {
	private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

	protected static CommandLine commandLine;

	public static CommandLine getCommandLine() {
		return commandLine;
	}

	public static void setCommandLine(CommandLine commandLine) {
		Main.commandLine = commandLine;
	}

	public static File getFile() {
		return FileUtils.toFile(Main.class.getProtectionDomain().getCodeSource().getLocation());
	}

	public static String getFileName() {
		return getFile().getAbsolutePath();
	}

	public static Options getOptions() {
		Options options = new Options();

		options.addOption(Option.builder("h")
			.longOpt("help")
			.desc("print this help message and exit")
			.build()
		);

		options.addOption(Option.builder("e")
			.longOpt("encrypt")
			.build()
		);

		options.addOption(Option.builder("d")
			.longOpt("decrypt")
			.build()
		);

		options.addOption(Option.builder("k")
			.longOpt("key")
			.desc("public/private key file for encryption/decryption")
			.hasArg()
			.argName("file")
			.build()
		);

		options.addOption(Option.builder("i")
			.longOpt("in")
			.desc("input file")
			.hasArg()
			.argName("file")
			.build()
		);

		options.addOption(Option.builder("o")
			.longOpt("out")
			.desc("output file")
			.hasArg()
			.argName("file")
			.build()
		);

		return options;
	}

	public static void parseCommandLine(String[] args) throws ParseException {
		CommandLineParser commandLineParser = new DefaultParser();
		setCommandLine(commandLineParser.parse(Main.getOptions(), args));
	}

	public static String getCommandLineSyntax() {
		return "java -jar " + FilenameUtils.getName(getFileName());
	}

	public static void printHelpMessage() {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(getCommandLineSyntax(), getOptions(), true);
	}

	public static byte[] readFile(String fileName) {
		LOGGER.info("reading from file '{}'", fileName);
		try {
			byte[] data = FileUtils.readFileToByteArray(new File(fileName));
			return data;
		}
		catch (IOException e) {
			LOGGER.error("error reading from file '{}' ({})", fileName, e.getMessage());
		}
		return null;
	}

	public static boolean writeFile(String fileName, byte[] data) {
		LOGGER.info("writing to output file '{}'", fileName);
		try {
			FileUtils.writeByteArrayToFile(new File(fileName), data);
			return true;
		}
		catch (IOException e) {

		}
		return false;
	}

	public static void main(String[] args) {
		try {
			parseCommandLine(args);

			if (null == getCommandLine()) {
				LOGGER.error("error parsing command line (null)");
				System.exit(1);
			}
			else if (getCommandLine().hasOption("help")) {
				printHelpMessage();
				System.exit(0);
			}
		}
		catch (ParseException e) {
			LOGGER.error("error parsing command line ({})", e.getMessage());
			System.exit(1);
		}

		if (getCommandLine().hasOption("encrypt")) {
			LOGGER.info("encrypt");
			Encrypt encrypt = new Encrypt();

			// read and set public key
			LOGGER.info("reading public key");
			String publicKeyFileName = getCommandLine().getOptionValue("key");
			byte[] publicKeyData = readFile(publicKeyFileName);
			LOGGER.info("setting public key");
			encrypt.setPublicKey(publicKeyData);

			// read input
			LOGGER.info("reading input");
			String inputFileName = getCommandLine().getOptionValue("input");
			byte[] inputData = readFile(inputFileName);

			// encrypt
			LOGGER.info("encrypting");
			byte[] outputData = encrypt.encrypt(inputData);

			// write output
			LOGGER.info("writing output");
			String outputFileName = getCommandLine().getOptionValue("output");
			writeFile(outputFileName, outputData);
		}
		else if (getCommandLine().hasOption("decrypt")) {
			LOGGER.info("decrypt");
			Decrypt decrypt = new Decrypt();

			// read and set private key
			LOGGER.info("reading private key");
			String privateKeyFileName = getCommandLine().getOptionValue("key");
			byte[] privateKeyData = readFile(privateKeyFileName);
			LOGGER.info("setting private key");
			decrypt.setPrivateKey(privateKeyData);

			// read input
			LOGGER.info("reading input");
			String inputFileName = getCommandLine().getOptionValue("input");
			byte[] inputData = readFile(inputFileName);

			// decrypt
			LOGGER.info("decrypting");
			byte[] outputData = decrypt.decrypt(inputData);

			// write output
			LOGGER.info("writing output");
			String outputFileName = getCommandLine().getOptionValue("output");
			writeFile(outputFileName, outputData);
		}
		else {
			LOGGER.error("missing --encrypt or --decrypt");
			System.exit(1);
		}

		System.exit(0);
	}
}
