package pl.confitura.jelatyna;

import com.insightfullogic.lambdabehave.JunitSuiteRunner;
import org.assertj.core.api.Assertions;
import org.junit.runner.RunWith;

import static com.insightfullogic.lambdabehave.Suite.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JunitSuiteRunner.class)
public class TestSpec {
    {
        describe("Calculator", it -> {
            it.should("add 2 and 2", (e) -> {
                assertThat(2 + 2).isEqualTo(4);
            });

            it.should("add 2 and 3", (e) -> {
                assertThat(2 + 3).isEqualTo(5);
            });
        });
    }
}
