package fr.insee.rem.domain.exception;

public class PartitionAlreadyExistsException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 3783013039351734977L;

    public PartitionAlreadyExistsException(String label) {
        super(String.format("Partition [%s] already exists", label));
    }

}
