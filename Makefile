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
CUP = cup
JAVAC = javac
JAVA = java

# Archivos fuente
LEXER = $(JFLEX_DIR)/RecetarioLexer.flex
PARSER = $(CUP_DIR)/RecetarioParser.cup
JAVA_SOURCES = $(MAIN_DIR)/*.java

# Archivos generados
LEXER_JAVA = $(OUTPUT_DIR)/RecetarioLexer.java
PARSER_JAVA = $(OUTPUT_DIR)/RecetarioParser.java
SYMBOLS = $(OUTPUT_DIR)/sym.java

# Clase principal
MAIN_CLASS = Main

.PHONY: all clean run test

all: directories lexer parser compile

directories:
	@mkdir -p $(OUTPUT_DIR)

lexer: $(LEXER)
	$(JFLEX) -d $(OUTPUT_DIR) $(LEXER)

parser: $(PARSER)
	$(CUP) -parser RecetarioParser -symbols sym -destdir $(OUTPUT_DIR) $(PARSER)

compile: $(LEXER_JAVA) $(PARSER_JAVA)
	$(JAVAC) -d $(OUTPUT_DIR) -cp $(OUTPUT_DIR) $(JAVA_SOURCES) $(OUTPUT_DIR)/*.java

run: all
	$(JAVA) -cp $(OUTPUT_DIR) $(MAIN_CLASS) $(EXAMPLES_DIR)/recetario.txt

clean:
	rm -rf $(OUTPUT_DIR)/*.class $(OUTPUT_DIR)/*.java

test: all
	@echo "Ejecutando pruebas..."
	@for file in $(EXAMPLES_DIR)/*.txt; do \
		echo "Probando: $$file"; \
		$(JAVA) -cp $(OUTPUT_DIR) $(MAIN_CLASS) $$file; \
	done

