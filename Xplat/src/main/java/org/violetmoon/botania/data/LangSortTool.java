/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.data;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple commandline tool for homogenizing all language files' layouts based on the ordering of translation keys in
 * {@code en_us.json}. Unknown and duplicate keys are moved to the end for further (manual) analysis.
 * The tool also strips all translation keys from translated language files where the translated text is identical to
 * the en_us version.
 */
public class LangSortTool {
	private static final Logger LOGGER = LoggerFactory.getLogger(LangSortTool.class);
	private static final Pattern JSON_LINE_PATTERN = Pattern
			.compile("\\s*\"(?<key>[^\"]+)\"\\s*:\\s*\"(?<value>(?:[^\"]|\\\\\")*)\",?\\s*");
	private static final String KEY_VALUE_LINE_FORMAT = "  \"%s\": \"%s\",";
	private static final Pattern PREAMBLE_COMMENT_KEY_PATTERN = Pattern.compile("_comment(?:\\d|UnmatchedBelow)");
	private static final List<String> FILE_PREAMBLE_COMMENTS = List.of(
			"Note to translators: DO NOT COPY ENTRIES FROM ENGLISH",
			"I repeat: do NOT copy entries from English!!",
			"Copying English entries will cause them to become outdated when this file changes",
			"Untranslated entries will automatically be inherited from this file.",
			"Once again, don't copy from English, and THANK YOU for your hard work!"
	);
	private static final String UNMATCHED_KEYS_BELOW_COMMENT = "Keys below this line don't match anything in the English language file:";

	public static void main(String[] args) throws IOException {
		final Path langDir = Path.of("Xplat/src/main/resources/assets/botania/lang").toAbsolutePath();
		LOGGER.info("Language directory: {}", langDir);

		final Map<String, String> enUsEntries = new HashMap<>();
		final Object2IntMap<String> keyLines = readEnUsLanguageFile(langDir, enUsEntries);
		final int lastEntryLine = keyLines.values().intStream().max().orElse(0) + 1;

		try (var files = Files.list(langDir)) {
			files.forEach(langFile -> processLanguageFile(langFile, lastEntryLine, enUsEntries, keyLines));
		}
	}

	private static Object2IntMap<String> readEnUsLanguageFile(Path langDir, Map<String, String> enUsEntries)
			throws IOException {
		final Path enUsFile = langDir.resolve("en_us.json");
		final List<String> enUsLines = Files.readAllLines(enUsFile, StandardCharsets.UTF_8);
		final Object2IntMap<String> keyLines = new Object2IntOpenHashMap<>(enUsLines.size());
		for (int i = 1; i < enUsLines.size() - 1; i++) {
			final String line = enUsLines.get(i);
			final Matcher lineMatcher = JSON_LINE_PATTERN.matcher(line);
			if (lineMatcher.matches()) {
				final String key = lineMatcher.group("key");
				keyLines.put(key, i);
				enUsEntries.put(key, lineMatcher.group("value"));
			}
		}
		return keyLines;
	}

	private static void processLanguageFile(Path langFile, int lastEntryLine, Map<String, String> enUsEntries,
			Object2IntMap<String> keyLines) {
		if (langFile.endsWith("en_us.json")) {
			LOGGER.info("Skipping English language file, it's the reference.");
			return;
		}

		LOGGER.info("Processing {}", langFile.getFileName());
		final List<String> langLines;
		try {
			langLines = Files.readAllLines(langFile, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		final int maxLines = Math.max(lastEntryLine, langLines.size());
		final List<String> newLangLines = new ArrayList<>(maxLines);
		for (int i = 0; i <= lastEntryLine + 1; i++) {
			newLangLines.add("");
		}

		for (int lineNo = 0; lineNo < langLines.size(); lineNo++) {
			final String line = langLines.get(lineNo);
			Matcher lineMatcher = JSON_LINE_PATTERN.matcher(line);
			if (lineMatcher.matches()) {
				final String key = lineMatcher.group("key");
				if (!PREAMBLE_COMMENT_KEY_PATTERN.matcher(key).matches()) {
					final String value = lineMatcher.group("value");
					if (value.equals(enUsEntries.get(key))) {
						LOGGER.warn("{}[{}]: Dropping copied value for '{}'", langFile.getFileName(), lineNo, key);
						continue;
					}
					if (keyLines.containsKey(key)) {
						final int lineIndex = keyLines.getInt(key);
						if (newLangLines.get(lineIndex).isEmpty()) {
							final String formattedLine = KEY_VALUE_LINE_FORMAT.formatted(key, value);
							newLangLines.set(lineIndex, formattedLine);
						} else {
							LOGGER.warn("{}[{}]: Duplicate key '{}'", langFile.getFileName(), lineNo, key);
							final String formattedLine = KEY_VALUE_LINE_FORMAT.formatted("_" + key, value);
							newLangLines.add(formattedLine);
						}
					} else {
						LOGGER.warn("{}}[{}}]: Unknown key '{}'", langFile.getFileName(), lineNo, key);
						final String formattedLine = KEY_VALUE_LINE_FORMAT.formatted(key, value);
						newLangLines.add(formattedLine);
					}
				}
			} else if (!line.isBlank() && !line.equals("{") && !line.equals("}")) {
				LOGGER.error("{}[{}]: Not a key/value pair - {}", langFile.getFileName(), lineNo, line);
			}
		}

		for (int i = 0; i < FILE_PREAMBLE_COMMENTS.size(); i++) {
			final String commentLine = KEY_VALUE_LINE_FORMAT.formatted("_comment" + i, FILE_PREAMBLE_COMMENTS.get(i));
			newLangLines.set(i + 1, commentLine);
		}
		while (newLangLines.size() > lastEntryLine && newLangLines.getLast().isEmpty()) {
			newLangLines.removeLast();
		}
		if (newLangLines.size() > lastEntryLine + 1) {
			newLangLines.set(lastEntryLine + 1,
					KEY_VALUE_LINE_FORMAT.formatted("_commentUnmatchedBelow", UNMATCHED_KEYS_BELOW_COMMENT));
		}
		for (int i = newLangLines.size() - 1; i > 0; i--) {
			final String line = newLangLines.get(i);
			if (!line.isEmpty()) {
				// trim final line's comma
				newLangLines.set(i, line.substring(0, line.length() - 1));
				break;
			}
		}
		newLangLines.set(0, "{");
		newLangLines.add("}");

		try {
			Files.write(langFile, newLangLines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
