// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 07/07/2020 15:21:50
// ******************************************************* 
package com.sy599.game.qipai.pdkuai.robot.conditions;

/** ModelCondition class created from MMPM condition HasBDanpai. */
public class HasBDanpai extends jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of HasBDanpai. */
	public HasBDanpai(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a
	 * com.sy599.game.qipai.pdkuai.robot.conditions.execution.HasBDanpai task
	 * that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.sy599.game.qipai.pdkuai.robot.conditions.execution.HasBDanpai(
				this, executor, parent);
	}
}