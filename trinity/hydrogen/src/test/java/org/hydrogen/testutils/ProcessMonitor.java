/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.testutils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO documentation
/**
 * Development code. Currently unused.
 * <p>
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ProcessMonitor {
	private final BufferedOutputStream processStdIn;
	private final BufferedReader processStdOut;
	private final BufferedReader processErr;
	private final Process monitoredProcess;

	private final ExecutorService processStdOutService;
	private final ExecutorService processErrService;
	private final ExecutorService processStoppedService;

	private boolean redirectStdOut;
	private boolean redirectErr;

	private volatile boolean monitoring;
	private volatile boolean processStopped;
	private volatile int processExitValue;

	/**
	 * 
	 * @param monitoredProcess
	 */
	public ProcessMonitor(final Process monitoredProcess) {
		this.monitoredProcess = monitoredProcess;

		this.processStdIn = new BufferedOutputStream(
				this.monitoredProcess.getOutputStream());
		this.processStdOut = new BufferedReader(
				new InputStreamReader(new BufferedInputStream(
						this.monitoredProcess.getInputStream())));
		this.processErr = new BufferedReader(
				new InputStreamReader(new BufferedInputStream(
						this.monitoredProcess.getErrorStream())));

		this.processStdOutService = Executors.newSingleThreadExecutor();
		this.processErrService = Executors.newSingleThreadExecutor();
		this.processStoppedService = Executors.newSingleThreadExecutor();

		startProcessStoppedService();
	}

	/**
	 * 
	 * @param monitoredProcess
	 * @param redirectStdOut
	 * @param redirectErr
	 * @see {@link ProcessMonitor#redirect(boolean, boolean)}
	 */
	public ProcessMonitor(final Process monitoredProcess,
			final boolean redirectStdOut, final boolean redirectErr) {
		this(monitoredProcess);
		redirect(redirectStdOut, redirectErr);
	}

	/**
	 * 
	 * @return
	 */
	public Process getMonitoredProcess() {
		return this.monitoredProcess;
	}

	/**
	 * 
	 * @return
	 */
	public BufferedReader getProcessErr() {
		return this.processErr;
	}

	/**
	 * 
	 * @return
	 */
	public int getProcessExitValue() {
		return this.processExitValue;
	}

	/**
	 * 
	 * @return
	 */
	public BufferedOutputStream getProcessStdIn() {
		return this.processStdIn;
	}

	/**
	 * 
	 * @return
	 */
	public BufferedReader getProcessStdOut() {
		return this.processStdOut;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isMonitoring() {
		return this.monitoring;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isProcessStopped() {
		return this.processStopped;
	}

	/**
	 * Redirect the monitored process' standard out and error output stream to
	 * this java program's standard out and error output stream.
	 * <p>
	 * Disabling any of the output streams does not stop or pauses output to
	 * this monitor. It merely ignores output that otherwise would be
	 * redirected.
	 * 
	 * @param redirectStdOut
	 *            redirect the program's standard out.
	 * @param redirectErr
	 *            redirect the program's error output stream.
	 */
	public void redirect(final boolean redirectStdOut, final boolean redirectErr) {
		this.redirectStdOut = redirectStdOut;
		this.redirectErr = redirectErr;
	}

	/**
	 * 
	 */
	private void startErrService() {
		this.processErrService.submit(new Runnable() {
			@Override
			public void run() {
				Thread.currentThread().setName(
						String.format("%s err reader.",
								ProcessMonitor.class.getSimpleName()));
				try {
					String line;
					while (((line = getProcessErr().readLine()) != null)
							&& isMonitoring()) {
						if (ProcessMonitor.this.redirectErr) {
							System.err.println(line);
						}
					}
					// getProcessErr().close();
				} catch (final IOException e) {
					ProcessMonitor.this.monitoring = false;
					throw new RuntimeException(e);
				}
			}
		});
	}

	/**
	 * 
	 */
	public void startProcessMonitor() {
		if (!isMonitoring()) {
			this.monitoring = true;
			startStdOutService();
			startErrService();
		}
	}

	/**
	 * 
	 */
	private void startProcessStoppedService() {
		this.processStoppedService.submit(new Runnable() {
			@Override
			public void run() {
				Thread.currentThread().setName(
						String.format("%s wait for.",
								ProcessMonitor.class.getSimpleName()));
				try {
					ProcessMonitor.this.processExitValue = ProcessMonitor.this.monitoredProcess
							.waitFor();
					ProcessMonitor.this.monitoring = false;
				} catch (final InterruptedException e) {
					ProcessMonitor.this.processStopped = true;
					throw new RuntimeException(e);
				}
			}
		});
	}

	/**
	 * 
	 */
	private void startStdOutService() {
		this.processStdOutService.submit(new Runnable() {
			@Override
			public void run() {
				Thread.currentThread().setName(
						String.format("%s stdOut reader.",
								ProcessMonitor.class.getSimpleName()));
				try {
					String line;
					while (((line = getProcessStdOut().readLine()) != null)
							&& isMonitoring()) {
						if (ProcessMonitor.this.redirectStdOut) {
							System.out.println(line);
						}
					}
					// getProcessStdOut().close();
				} catch (final IOException e) {
					ProcessMonitor.this.monitoring = false;
					throw new RuntimeException(e);
				}
			}
		});
	}

	/**
	 * Stops the monitoring of the <code>Process</code>.
	 */
	public void stopProcessMonitor() {
		this.processErrService.shutdown();
		this.processStdOutService.shutdown();
		// TODO does this give an interrupted exception?
		this.processStoppedService.shutdownNow();
		this.monitoring = false;
	}
}
