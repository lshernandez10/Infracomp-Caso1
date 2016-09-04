import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Main 
{
	public static void main(String[] args)
	{
		try 
		{
			//Se lee el archivo que contiene el n�mero de clientes, 
			//el n�mero de servidores, el n�mero de consultas de cada cliente 
			//y el tama�o del buffer
			File file = new File("doc/config.properties");
			FileInputStream fileInput;
			fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);

			int numeroClientes = Integer.parseInt( properties.getProperty("numeroClientes") );
			int numeroServidores = Integer.parseInt( properties.getProperty("numeroServidores") );
			int consultasPorCliente = Integer.parseInt( properties.getProperty("consultasPorCliente") );
			int tama�oBuffer = Integer.parseInt( properties.getProperty("capacidad") );
			fileInput.close();
			
			System.out.println(" numeroClientes: "+ numeroClientes + "\n numeroServidores: " + numeroServidores + "\n consultas por cliente: " + consultasPorCliente + "\n capadidad: "+ tama�oBuffer);
			
			//Se crea el buffer
			Buffer buffer = new Buffer(tama�oBuffer);
			
			//Se crean los clientes
			ArrayList<Cliente> clientes = new ArrayList<>(numeroClientes);
			for (int i = 0; i < numeroClientes; i++) 
			{
				clientes.add(new Cliente(consultasPorCliente, buffer));
			}
			
			//Se crean los servidores
			ArrayList<Servidor> servidores = new ArrayList<>(numeroServidores);
			for (int i = 0; i < numeroServidores; i++) 
			{
				servidores.add(new Servidor(buffer));
			}
			
			//Se le asignan los clientes y servidores al buffer
			buffer.setClientes(clientes);
			buffer.setServidores(servidores);
			
			//se inician los threads
			buffer.comenzarEjecucion();
			
			System.out.println("FIN");
		} 
		
		catch(IOException e) 
		{
			e.printStackTrace();
		}
		
	}
}
