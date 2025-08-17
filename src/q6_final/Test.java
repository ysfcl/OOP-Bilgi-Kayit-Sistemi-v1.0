package q6_final;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.io.*;


interface IYazdirilabilir{
	void yazdir();
}


abstract class Kullanici{
	String ad_soyad,eposta;
	int yas;
}

class Adres implements Cloneable{
	String sehir;
	
	@Override
	public Object clone()throws CloneNotSupportedException {
		return super.clone();
	}
	
	@Override
	public String toString() {
		return sehir;
	}
}

class Pencere extends JFrame{
	
	JTextField[] jtf;
	JLabel[] jlbl;
	JComboBox kombo;
	JCheckBox[] jcb;
	JRadioButton[] jrb;
	JButton buton;
	JPanel[] panel;
	
	Pencere(){
		super("Kayit sistemi");
		
		int en=400,boy=600;
		
		this.setSize(en,boy);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		JPanel formPanel=new JPanel(new GridLayout(6,2,5,5));
			
		JPanel paneller[]=new JPanel[12];
			
		for(int i=0;i<paneller.length;i++) {
			paneller[i]=new JPanel();
			formPanel.add(paneller[i]);
		}
		//formPanel.setBackground(Color.GRAY);
		this.add(formPanel,BorderLayout.CENTER);
						
		this.jlbl=new JLabel[6];
		jlbl[0]=new JLabel("Ad-Soyad:");
		paneller[0].add(jlbl[0]);
		
		this.jtf=new JTextField[3];
		jtf[0]=new JTextField(20);
		jtf[0].setPreferredSize(new Dimension(150,25));
		paneller[1].add(jtf[0]);
		
		jlbl[1]=new JLabel("Yas:");
		paneller[2].add(jlbl[1]);
		
		jtf[1]=new JTextField(20);
		jtf[1].setPreferredSize(new Dimension(150,25));
		paneller[3].add(jtf[1]);
		
		jlbl[2]=new JLabel("E-posta:");
		paneller[4].add(jlbl[2]);
		
		jtf[2]=new JTextField(20);
		jtf[2].setPreferredSize(new Dimension(150,25));
		paneller[5].add(jtf[2]);
		
	
		jlbl[3]=new JLabel("Cinsiyet");
		paneller[6].add(jlbl[3]);
		
		this.jrb=new JRadioButton[2];
		ButtonGroup bg=new ButtonGroup();
		
		String[] cinsiyet={"Erkek","Kadin"};
		
		for(int i=0;i<jrb.length;i++) {
			jrb[i]=new JRadioButton(cinsiyet[i]);
			bg.add(jrb[i]);
			paneller[7].add(jrb[i]);
		}
		
		jlbl[4]=new JLabel("Sehir:");
		paneller[8].add(jlbl[4]);
		
		String[] sehir= {"Bursa","Ankara","Istanbul","Izmir"};
		this.kombo=new JComboBox<>(sehir);
		paneller[9].add(kombo);
		
		
		jlbl[5]=new JLabel("Ilgi alanlari:");
		paneller[10].add(jlbl[5]);
	
		
		this.jcb=new JCheckBox[2];
		String[] ilgi= {"Muzik","Spor"};
		
		for(int i=0;i<jcb.length;i++) {
			jcb[i]=new JCheckBox(ilgi[i]);
			paneller[11].add(jcb[i]);
		}
		
		
		buton=new JButton("Gonder");
		JPanel butonPanel=new JPanel();
		
		butonPanel.add(buton);
		this.add(butonPanel,BorderLayout.SOUTH);
				
		buton.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String ad_soyad,e_posta,yasStr,cinsiyet;
				Adres sehir;
				int yas;
				
				ad_soyad=jtf[0].getText();
				
				yasStr=jtf[1].getText();
				yas=Integer.parseInt(yasStr);
				
				e_posta=jtf[2].getText();
				
				try {
				if(yas<18) {
					JOptionPane.showMessageDialog(null,"Yas 18'den kucuk olamaz!");
				}
				
				if(e_posta.trim().isEmpty()||!e_posta.contains("@")) {
					JOptionPane.showMessageDialog(null,"E-posta bos birakilmis ya da hatali girilmis!");
				}
				
				
				if(jrb[0].isSelected()) {
					cinsiyet="Erkek";
				}
				
				else if(jrb[1].isSelected()) {
					cinsiyet="Kadin";
				}
				
				else {
					cinsiyet="Belirtilmedi";
				}
				
				Adres adr=new Adres();
				adr.sehir=(String)kombo.getSelectedItem();
				
				StringBuilder sb=new StringBuilder();
				
			    if(jcb[0].isSelected()&& jcb[1].isSelected()) {
					sb.append("Muzik");
					sb.append(" ve Spor");
				}
				
				else if(jcb[0].isSelected()) {
					sb.append("Muzik");
				}
				
				else if(jcb[1].isSelected()) {
					sb.append("Spor");
				}
		
				else {
					sb.append("Ilgi alani secmediniz.");
				}
		
			    
				JOptionPane.showMessageDialog(null,"Ad-Soyad:"+ad_soyad+"\nE-posta:"+e_posta+"\nYas:"+yas+"\nCinsiyet:"+cinsiyet+"\nSehir:"+adr.sehir+"\nIlgi alani:"+sb);
				
				String ilgi_alani=sb.toString();
				
				Ogrenci o1=new Ogrenci(ad_soyad,e_posta,yas,cinsiyet,adr,ilgi_alani);
				Thread t1=new Thread(new KayitYazdir(o1));
					
				Ogrenci o2=(Ogrenci)o1.clone();
				Thread t2=new Thread(new KayitYazdir(o2));
				
				t1.start();
				t2.start();
				
				t1.join();
				t2.join();

				dispose();	//her zaman en sona
			}catch(Exception ex) {
				System.out.println("Hata:"+ex.getMessage());
			}
				
			}
		});
		
		this.setVisible(true);
				
	}
	
}

class KayitYazdir implements Runnable{
	
	private Ogrenci ogr;
	
	KayitYazdir(Ogrenci o1){
		this.ogr=o1;
	}
	
	@Override
	public void run() {		
		ogr.yazdir();
				
	}
	
}

class Ogrenci extends Kullanici implements IYazdirilabilir,Cloneable{
	
	Adres sehir;
	String cinsiyet,ilgi_alani;
	
	Ogrenci(){}
	
	Ogrenci(String ad_soyad,String e_posta,int yas,String cinsiyet,Adres sehir,String ilgi_alani){
		this.ad_soyad=ad_soyad;
		this.eposta=e_posta;
		this.yas=yas;
		this.cinsiyet=cinsiyet;
		this.sehir=sehir;
		this.ilgi_alani=ilgi_alani;
	}
	
	@Override
	public void yazdir() {
		try {
			File dosya=new File("kullanici_bilgisi.txt");
			PrintWriter pw=new PrintWriter(new FileWriter(dosya,true));	//true yazilmasinin sebebi uzerine yazilmamasi icin
			
			pw.println("OGRENCININ OZELLIKLERI:\n\n");
			pw.println("Ad-Soyad:"+ad_soyad+"\nE-posta:"+eposta+"\nYas:"+yas+"\nCinsiyet:"+cinsiyet+"\nSehir:"+sehir+"\nIlgi alani:"+ilgi_alani);
			
			BufferedReader bf=new BufferedReader(new FileReader(dosya));
			
			String satir;
			
			while((satir=bf.readLine())!=null) {
				System.out.println(satir);
			}
			
			pw.close();
			bf.close();
			
			}catch(Exception e) {
				System.out.println("Hata:"+e.getMessage());
			}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException{
		Ogrenci kopya=(Ogrenci)super.clone();
		kopya.sehir=(Adres)this.sehir.clone();
		return kopya;
	}
}


public class Test {
	public static void main(String[] args) {
		
		if(args.length>3) {
			String adSoyad=args[0];
			String eposta=args[1];
			int yas=Integer.parseInt(args[2]);
		
			Adres a=new Adres();
			a.sehir="Bilinmiyor";
			Ogrenci ogr=new Ogrenci(adSoyad,eposta,yas,"Belirtilmedi",a,"Bilinmiyor");
			ogr.yazdir();
		}
		
		else {
			Pencere x=new Pencere();
		}
	}
}