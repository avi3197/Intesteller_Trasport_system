package ITS_MainController.ValidationStubs;

import org.junit.Test;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.Assert.assertThat;

/**
 * Created by Kapeshi.Kongolo on 2016/04/18.
 */
public class ValidationCodesTest {
    @Test
    public void verifyThatValidationCodesHandlingIsCorrect() throws Exception {
        Validation expectedCode = Validation.ROUTE_EXISTS;
        int codeId = Validation.TRAFFIC_EXISTS.getId();
        Validation actualMode = Validation.fromString("ROUTE EXISTS");
        assertThat(expectedCode, sameBeanAs(actualMode));
        assertThat(3, sameBeanAs(codeId));
    }
}