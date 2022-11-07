package fr.insee.rem.exception;

public class SampleAlreadyExistsException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 3783013039351734977L;

    public SampleAlreadyExistsException(String label) {
        super(String.format("Sample [%s] already exists", label));
    }

}
