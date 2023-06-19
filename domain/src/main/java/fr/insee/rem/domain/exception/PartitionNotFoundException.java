package fr.insee.rem.domain.exception;

public class PartitionNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 3783013039351734977L;

    public PartitionNotFoundException(Long id) {
        super(String.format("Partition [%s] doesn't exist", id));
    }

}
