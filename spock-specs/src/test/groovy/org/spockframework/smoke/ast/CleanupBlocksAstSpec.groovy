package org.spockframework.smoke.ast

import org.spockframework.EmbeddedSpecification
import spock.lang.Issue
import spock.util.Show

class CleanupBlocksAstSpec extends EmbeddedSpecification {

  @Issue("https://github.com/spockframework/spock/issues/1266")
  def "cleanup rewrite keeps correct method reference"() {
    when:
    def result = compiler.transpileSpecBody('''
def "cleanup blocks don't destroy method reference when invocation is assigned to variable with the same name"() {
  when:
  def foobar = foobar()

  then:
  println(foobar)

  cleanup:
  foobar.size()
}

def foobar() {
  return "foo"
}''', EnumSet.of(Show.METHODS))
    then:
    result.source == '''\
public java.lang.Object foobar() {
    return 'foo'
}

public void $spock_feature_0_0() {
    org.spockframework.runtime.ErrorCollector $spock_errorCollector = org.spockframework.runtime.ErrorRethrower.INSTANCE
    org.spockframework.runtime.ValueRecorder $spock_valueRecorder = new org.spockframework.runtime.ValueRecorder()
    java.lang.Object foobar
    java.lang.Throwable $spock_feature_throwable
    try {
        foobar = this.foobar()
        try {
            org.spockframework.runtime.SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), 'println(foobar)', 6, 3, null, this, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(0), 'println'), new java.lang.Object[]{$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(1), foobar)}, $spock_valueRecorder.realizeNas(4, false), false, 3)
        }
        catch (java.lang.Throwable $spock_condition_throwable) {
            org.spockframework.runtime.SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, 'println(foobar)', 6, 3, null, $spock_condition_throwable)}
        finally {
        }
    }
    catch (java.lang.Throwable $spock_tmp_throwable) {
        $spock_feature_throwable = $spock_tmp_throwable
        throw $spock_tmp_throwable
    }
    finally {
        try {
            foobar.size()
        }
        catch (java.lang.Throwable $spock_tmp_throwable) {
            if ( $spock_feature_throwable != null) {
                $spock_feature_throwable.addSuppressed($spock_tmp_throwable)
            } else {
                throw $spock_tmp_throwable
            }
        }
        finally {
        }
    }
    this.getSpecificationContext().getMockController().leaveScope()
}'''

  }

  @Issue("https://github.com/spockframework/spock/issues/1332")
  def "cleanup rewrite keeps correct method reference for multi-assignments"() {
    when:
    def result = compiler.transpileSpecBody('''
def "cleanup blocks don't destroy method reference when invocation is assigned to variable with the same name"() {
  when:
  def (foobar, b) = foobar()

  then:
  println(foobar)

  cleanup:
  foobar.size()
}

def foobar() {
  return ["foo", "bar"]
}''', EnumSet.of(Show.METHODS))
    then:
    result.source == '''\
public java.lang.Object foobar() {
    return ['foo', 'bar']
}

public void $spock_feature_0_0() {
    org.spockframework.runtime.ErrorCollector $spock_errorCollector = org.spockframework.runtime.ErrorRethrower.INSTANCE
    org.spockframework.runtime.ValueRecorder $spock_valueRecorder = new org.spockframework.runtime.ValueRecorder()
    def (java.lang.Object foobar, java.lang.Object b) = [null, null]
    java.lang.Throwable $spock_feature_throwable
    try {
        (foobar, b) = this.foobar()
        try {
            org.spockframework.runtime.SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), 'println(foobar)', 6, 3, null, this, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(0), 'println'), new java.lang.Object[]{$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(1), foobar)}, $spock_valueRecorder.realizeNas(4, false), false, 3)
        }
        catch (java.lang.Throwable $spock_condition_throwable) {
            org.spockframework.runtime.SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, 'println(foobar)', 6, 3, null, $spock_condition_throwable)}
        finally {
        }
    }
    catch (java.lang.Throwable $spock_tmp_throwable) {
        $spock_feature_throwable = $spock_tmp_throwable
        throw $spock_tmp_throwable
    }
    finally {
        try {
            foobar.size()
        }
        catch (java.lang.Throwable $spock_tmp_throwable) {
            if ( $spock_feature_throwable != null) {
                $spock_feature_throwable.addSuppressed($spock_tmp_throwable)
            } else {
                throw $spock_tmp_throwable
            }
        }
        finally {
        }
    }
    this.getSpecificationContext().getMockController().leaveScope()
}'''

  }
}
