package seifert.back.facility.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    GIVEN_FACILITY_NAME_ALREADY_EXISTS("Could not create facility. Given facility name already exists.");

    private final String message;
}
