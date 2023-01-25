package fr.insee.rem.domain.exception;

public class CsvFileException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 7497925308507192965L;

    public CsvFileException(String message) {
        super(message);
    }

    public CsvFileException(String message, Throwable t) {
        super(message, t);
    }

}
