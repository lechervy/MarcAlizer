package JKernelMachines.fr.lip6.kernel.extra.colt;

import JKernelMachines.fr.lip6.kernel.Kernel;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;

public class SparseDoubleLinear extends Kernel<SparseDoubleMatrix1D> {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3605052015116876466L;

	@Override
	public double valueOf(SparseDoubleMatrix1D t1, SparseDoubleMatrix1D t2) {
		
		if(t1.size() !=  t2.size())
			return 0;
		
		double sum = t1.zDotProduct(t2);
		
		return sum;
	}

	@Override
	public double valueOf(SparseDoubleMatrix1D t1) {
		
		return valueOf(t1, t1);
	}

}
