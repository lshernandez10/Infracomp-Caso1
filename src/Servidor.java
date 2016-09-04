
public class Servidor extends Thread
{
	/**
	 * Canal por el que se reciben mensajes
	 */
	private Buffer canal;
	
	/**
	 * Mensaje que se está respondiendo actualmente
	 */
	private Mensaje mensaje;
	
	public Servidor(Buffer pCanal) 
	{
		canal = pCanal;
	}
	
	/**
	 * Se encarga de solicitar y responder mensajes al Buffer
	 */
	public synchronized void solicitarResponderMensaje()
	{
		mensaje = canal.enviarMensajeServidor();
		if(mensaje != null)
		{
			mensaje.setContestado(true);
			System.out.println("Servidor respondió");
		}
	}
	
	@Override
	public void run() 
	{
		boolean hayClientes = true;
		while(hayClientes)
		{
			synchronized(this)
			{
				while(mensaje == null && hayClientes)
				{
					hayClientes = canal.getClientes().size() > 0;
					solicitarResponderMensaje();
					yield();
				}
			}
			
			if(mensaje != null && hayClientes)
			{
				synchronized(this)
				{
					hayClientes = canal.getClientes().size() > 0;
					mensaje.notificarAlCliente();
					mensaje = null;
				}
			}
			
			hayClientes = canal.getClientes().size() > 0;
		}
	}
}
