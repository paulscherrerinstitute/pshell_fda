package ch.psi.fda;

public interface EContainer {

	/**
	 * Initialize execution container like required resources, etc.
	 */
	public void initialize();
	
	/**
	 * Executes the logic implemented by the ExecutionContainer
	 * Execute is a blocking function and must not return before the actual logic is executed
	 */
	public void execute();
	
	/**
	 * Try to abort the execution of the logic
	 */
	public void abort();
	
	public boolean isActive();
	
	/**
	 * Destroy execution container and free all allocated resources
	 */
	public void destroy();
	
}
