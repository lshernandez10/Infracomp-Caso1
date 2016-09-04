
public class Cliente extends Thread
{	
	//ATRIBUTOS
	
	/**
	 * Total mensajes para enviar
	 */
	private int totalMensajes;
	
	/**
	 * Indica el número de mensajes que faltan por enviar.
	 */
	private int mensajesPorEnviar;
	
	/**
	 * Mensajes que han sido respondidos
	 */
	private int mensajesRespondidos;
	
	/**
	 * Canal por el que el cliente envía los mensajes.
	 */
	private Buffer canal; 
	
	//CONSTRUCTOR
	
	public Cliente(int numeroMensajes, Buffer pCanal) 
	{
		totalMensajes = numeroMensajes;
		mensajesPorEnviar = numeroMensajes;
		mensajesRespondidos = 0;
		canal = pCanal;
	}
	
	//MÉTODOS
	
	public boolean enviarMensaje()
	{
		Mensaje mensaje = new Mensaje(this);
		boolean enviado = canal.recibirMensajeDeCliente(mensaje);
		
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
				System.out.println("El cliente " + getId() +" envió un mensaje");
				respondido = enviarMensaje();
				yield();
			}
			mensajesPorEnviar--;
//			System.out.println("El mensaje "+ (mensajesRespondidos+1) + " del cliente " + getId() + " está en el canal");
			
			synchronized (this) 
			{
				try 
				{
					wait();
					
					System.out.println("Mensaje respondido " + (mensajesRespondidos+1)+ " del cliente " + getId());
					
					//Aumenta la cantidad de mensajes respondidos y si es igual 
					//al número total de mensajes se termina la conexión con el Buffer 
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
