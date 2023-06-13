package fr.insee.rem.domain.exception;

public class SampleNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 3783013039351734977L;

    public SampleNotFoundException(Long id) {
        super(String.format("Sample [%s] doesn't exist", id));
    }

}
