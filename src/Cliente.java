
public class Cliente extends Thread
{	
	//ATRIBUTOS
	
	/**
	 * Total mensajes para enviar
	 */
	private int totalMensajes;
	
	/**
	 * Indica el n�mero de mensajes que faltan por enviar.
	 */
	private int mensajesPorEnviar;
	
	/**
	 * Mensajes que han sido respondidos
	 */
	private int mensajesRespondidos;
	
	/**
	 * Canal por el que el cliente env�a los mensajes.
	 */
	private Buffer canal; 
	
	/**
	 * Es el mensaje actual del cliente
	 */
	private Mensaje mensajeActual;
	
	//CONSTRUCTOR
	
	public Cliente(int numeroMensajes, Buffer pCanal) 
	{
		totalMensajes = numeroMensajes;
		mensajesPorEnviar = numeroMensajes;
		mensajesRespondidos = 0;
		canal = pCanal;
	}
	
	//M�TODOS
	
	public synchronized boolean enviarMensaje()
	{
		if(mensajeActual == null)
			mensajeActual = new Mensaje(this);
		boolean enviado = canal.recibirMensajeDeCliente(mensajeActual);
		
		return enviado;
	}
	
	@Override
	public void run() 
	{
		while(mensajesPorEnviar > 0)
		{
			boolean respondido = false;
			while(!respondido)
			{
				System.out.println("El cliente " + getId() +" envi� un mensaje");
				respondido = enviarMensaje();
				yield();
			}
			mensajeActual = null;
			mensajesPorEnviar--;
//			System.out.println("El mensaje "+ (mensajesRespondidos+1) + " del cliente " + getId() + " est� en el canal");
			
			synchronized (this) 
			{
				try 
				{
					wait();
					
					System.out.println("Mensaje respondido " + (mensajesRespondidos+1)+ " del cliente " + getId());
					
					//Aumenta la cantidad de mensajes respondidos y si es igual 
					//al n�mero total de mensajes se termina la conexi�n con el Buffer 
					if(++mensajesRespondidos == totalMensajes)
					{
						canal.retirarCliente(this);
						System.err.println("Cliente " + getId() +  " retirado");
					}
				} 
				catch(InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}
