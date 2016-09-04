import java.util.ArrayList;

public class Buffer 
{
	//ATRIBUTOS 
	
	/**
	 * Clientes del canal
	 */
	private ArrayList<Cliente> clientes;
	
	/**
	 * Servidores que atenderan las peticiones de los clientes
	 */
	private ArrayList<Servidor> servidores;
	
	/**
	 * Lista de mensajes que est�n en el buffer esperando a ser respondidos por un servidor
	 */
	private ArrayList<Mensaje> mensajes;
	
	private int tama�o;
	
	
	//CONSTRUCTOR
	
	public Buffer(int pTama�o) 
	{
		tama�o = pTama�o;
		mensajes = new ArrayList<>(pTama�o);
	}
	
	//M�TODOS
	
	public ArrayList<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(ArrayList<Cliente> clientes) {
		this.clientes = clientes;
	}

	public ArrayList<Servidor> getServidores() {
		return servidores;
	}

	public void setServidores(ArrayList<Servidor> servidores) {
		this.servidores = servidores;
	}
	
	/**
	 * M�todo que se encarga de crear a los clientes y servidores y comenzar
	 * la ejecuci�n del programa
	 */
	public void comenzarEjecucion()
	{
		//Inicializa los threads de clientes
		for(int i = 0; i < clientes.size(); i++)
		{
			clientes.get(i).start();
		}

		//Inicializa los threads de servidores
		for(int i = 0; i < servidores.size(); i++)
		{
			servidores.get(i).start();
		}
	}

	/**
	 * M�todo que llama el cliente cuando va a enviar un mensaje
	 * @param mensaje
	 * @return
	 */
	public synchronized boolean recibirMensajeDeCliente(Mensaje mensaje)
	{
		boolean recibido = false;
		synchronized (mensajes) 
		{
			if(mensajes.size() < tama�o)
			{
				mensajes.add(mensaje);
				recibido = true;
			}
			if(!recibido)
				System.err.println("se llen� la cola");
		}
		return recibido;
	}

	/**
	 * Retira a un cliente del canal en cuanto el lo solicita
	 * @param cliente
	 */
	public synchronized void retirarCliente(Cliente cliente)
	{
		for (int i = 0; i < clientes.size(); i++) 
		{
			if(clientes.get(i).getId() == cliente.getId())
				clientes.remove(i);
		}
	}
	
	/**
	 * Se ejecuta cuando un servidor solicita un mensaje
	 * @return mensaje
	 */
	public synchronized Mensaje enviarMensajeServidor()
	{
		Mensaje mensaje = null;
		synchronized(mensajes)
		{
			if(!mensajes.isEmpty())
			{
				mensaje = mensajes.get(0);
				mensajes.remove(0);
			}
		}
		return mensaje;
	}
}