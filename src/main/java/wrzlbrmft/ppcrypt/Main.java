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

		options.addOption(Option.builder("k")
			.longOpt("key")
			.desc("public/private key file for encryption/decryption")
			.build()
		);

		options.addOption(Option.builder("i")
			.longOpt("in")
			.desc("input file")
			.build()
		);

		options.addOption(Option.builder("o")
			.longOpt("out")
			.desc("output file")
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

		LOGGER.info("Hello World!");
	}
}
