<?php

class Constants
{
    //DATABASE DETAILS
    static $DB_SERVER="localhost";
    static $DB_NAME="cisostor_store";
    static $USERNAME="cisostor_store";
    static $PASSWORD="12345";

    //STATEMENTS
    static $SQL_SELECT_ALL="SELECT * FROM produk";
}

class Spacecrafts
{
    /*******************************************************************************************************************************************/
    /*
       1.CONNECT TO DATABASE.
       2. RETURN CONNECTION OBJECT
    */
    public function connect()
    {
        $con=new mysqli(Constants::$DB_SERVER,Constants::$USERNAME,Constants::$PASSWORD,Constants::$DB_NAME);
        if($con->connect_error)
        {
            return null;
        }else
        {
            return $con;
        }
    }
    /*******************************************************************************************************************************************/
    /*
       1.SELECT FROM DATABASE.
    */
    public function search($query)
    {

        //$sql="SELECT * FROM produk WHERE nama LIKE '%$query%' OR deskripsi LIKE '%$query%' OR harga LIKE '%$query%' ";
		 $sql="SELECT * FROM produk WHERE nama LIKE '%$query%' ";

        $con=$this->connect();
        if($con != null)
        {
            $result=$con->query($sql);
            if($result->num_rows > 0)
            {
                $spacecrafts=array();
                while($row=$result->fetch_array())
                {
                    array_push($spacecrafts, array("id"=>$row['id'],"nama"=>$row['nama'],"deskripsi"=>$row['deskripsi'],"harga"=>$row['harga'],"berat"=>$row['berat'],"stok"=>$row['stok'],"gambar"=>$row['gambar']));
                }
                print(json_encode(array_reverse($spacecrafts)));
            }else
            {
                print(json_encode(array("No item Found that matches the query: ".$query)));
            }
            $con->close();

        }else{
            print(json_encode(array("PHP EXCEPTION : CAN'T CONNECT TO MYSQL. NULL CONNECTION.")));
        }
    }
    public function handleRequest() {
		if($_SERVER['REQUEST_METHOD'] == 'POST'){
			$query=$_POST['query'];
            $this->search($query);
        } else{
            $this->search("");
        }
	
    }
}
$spacecrafts=new Spacecrafts();
$spacecrafts->handleRequest();
//end
