package seifert.back.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityIDNotFoundException extends EntityNotFoundException {

    public EntityIDNotFoundException(String exception) {
        super(exception);
    }
}
