// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 07/07/2020 15:21:50
// ******************************************************* 
package com.sy599.game.qipai.pdkuai.robot.actions;

/** ModelAction class created from MMPM action ChuBduizi. */
public class ChuBduizi extends jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of ChuBduizi. */
	public ChuBduizi(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a com.sy599.game.qipai.pdkuai.robot.actions.execution.ChuBduizi
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.sy599.game.qipai.pdkuai.robot.actions.execution.ChuBduizi(
				this, executor, parent);
	}
}