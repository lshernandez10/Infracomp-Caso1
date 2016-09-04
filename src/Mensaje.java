
public class Mensaje 
{
	//ATRIBUTOS
	
	/**
	 * Cliente que envia el mensaje
	 */
	private Cliente cliente;
	
	/**
	 * Define si el mensaje fue contestado por un servidor o no
	 */
	private boolean contestado;
	
	//CONSTRUCTOR
	
	public Mensaje(Cliente pCliente) 
	{
		cliente = pCliente;
		contestado = false;
	}

	//M�TODOS
	
	public boolean isContestado() {
		return contestado;
	}

	public void setContestado(boolean contestado) {
		this.contestado = contestado;
	}
	
	public synchronized void notificarAlCliente()
	{
		synchronized(cliente)
		{
			cliente.notify();
		}
	}
}
