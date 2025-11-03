# Makefile para Analizador de Recetario Digital
# JFlex y CUP

# Directorios
JFLEX_DIR = src/jflex
CUP_DIR = src/cup
MAIN_DIR = src/main
OUTPUT_DIR = output
EXAMPLES_DIR = examples

# Compiladores
JFLEX = jflex
CUP_JAR = libs/java-cup-11b.jar
JAVA_HOME_BREW = /usr/local/Cellar/openjdk/25
JAVA_BIN = $(JAVA_HOME_BREW)/bin
CUP = $(JAVA_BIN)/java -jar $(CUP_JAR)
JAVAC = $(JAVA_BIN)/javac
JAVA = $(JAVA_BIN)/java

# Archivos fuente
LEXER = $(JFLEX_DIR)/RecetarioLexer.flex
PARSER = $(CUP_DIR)/RecetarioParser.cup
JAVA_SOURCES = $(MAIN_DIR)/*.java

# Archivos generados
LEXER_JAVA = $(OUTPUT_DIR)/RecetarioLexer.java
PARSER_JAVA = $(OUTPUT_DIR)/RecetarioParser.java
SYMBOLS = $(OUTPUT_DIR)/sym.java

# Clase principal
MAIN_CLASS = main.Main

# Librerías
LIBS = libs/java-cup-runtime.jar
CLASSPATH = $(OUTPUT_DIR):$(LIBS)

.PHONY: all clean run test

all: directories lexer parser compile

directories:
	@mkdir -p $(OUTPUT_DIR)

lexer: $(LEXER)
	$(JFLEX) -d $(OUTPUT_DIR) $(LEXER)

parser: $(PARSER)
	$(JAVA_BIN)/java -cp $(CUP_JAR) java_cup.Main -parser RecetarioParser -symbols sym -destdir $(OUTPUT_DIR) -expect 1 $(PARSER)

compile: $(LEXER_JAVA) $(PARSER_JAVA)
	$(JAVAC) -d $(OUTPUT_DIR) -cp "$(CLASSPATH)" $(MAIN_DIR)/*.java $(OUTPUT_DIR)/*.java

run: all
	$(JAVA) -cp "$(CLASSPATH)" $(MAIN_CLASS) $(EXAMPLES_DIR)/recetario.txt

clean:
	rm -rf $(OUTPUT_DIR)/*.class $(OUTPUT_DIR)/*.java

test: all
	@echo "Ejecutando pruebas..."
	@for file in $(EXAMPLES_DIR)/*.txt; do \
		echo "Probando: $$file"; \
		$(JAVA) -cp "$(CLASSPATH)" $(MAIN_CLASS) $$file; \
	done

