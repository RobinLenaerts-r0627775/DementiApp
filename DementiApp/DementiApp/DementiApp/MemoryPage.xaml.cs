using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DementiApp
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class MemoryPage : ContentPage
	{
        private Button clicked;
        Dictionary<string, Color> layout = new Dictionary<string, Color>();
		public MemoryPage ()
		{
            var numbers = new List<int>(Enumerable.Range(0, 24));
            numbers.Shuffle();
            foreach(int i in numbers)
            {
                Console.Write(""+i);
                switch (numbers.IndexOf(i))
                    {
                    case 0:
                        layout.Add("" + i, Color.Magenta);
                        break;
                    case 1:
                        layout.Add("" + i, Color.Magenta);
                        break;
                    case 2:
                        layout.Add("" + i, Color.LimeGreen);
                        break;
                    case 3:
                        layout.Add("" + i, Color.LimeGreen);
                        break;
                    case 4:
                        layout.Add("" + i, Color.Yellow);
                        break;
                    case 5:
                        layout.Add("" + i, Color.Yellow);
                        break;
                    case 6:
                        layout.Add("" + i, Color.Green);
                        break;
                    case 7:
                        layout.Add("" + i, Color.Green);
                        break;
                    case 8:
                        layout.Add("" + i, Color.OrangeRed);
                        break;
                    case 9:
                        layout.Add("" + i, Color.OrangeRed);
                        break;
                    case 10:
                        layout.Add("" + i, Color.White);
                        break;
                    case 11:
                        layout.Add("" + i, Color.White);
                        break;
                    case 12:
                        layout.Add("" + i, Color.Gray);
                        break;
                    case 13:
                        layout.Add("" + i, Color.Gray);
                        break;
                    case 14:
                        layout.Add("" + i, Color.MidnightBlue);
                        break;
                    case 15:
                        layout.Add("" + i, Color.MidnightBlue);
                        break;
                    case 16:
                        layout.Add("" + i, Color.Moccasin);
                        break;
                    case 17:
                        layout.Add("" + i, Color.Moccasin);
                        break;
                    case 18:
                        layout.Add("" + i, Color.YellowGreen);
                        break;
                    case 19:
                        layout.Add("" + i, Color.YellowGreen);
                        break;
                    case 20:
                        layout.Add("" + i, Color.Olive);
                        break;
                    case 21:
                        layout.Add("" + i, Color.Olive);
                        break;
                    case 22:
                        layout.Add("" + i, Color.Orange);
                        break;
                    case 23:
                        layout.Add("" + i, Color.Orange);
                        break;
                    default:
                        layout.Add("" + i, Color.Black);
                        break;
                    }
            }
			InitializeComponent ();
		}

        private void Button_Clicked(object sender, EventArgs e)
        {
            Button but = (Button)sender;
            but.IsEnabled = false;
            Color col = Color.Black;
            layout.TryGetValue(but.StyleId, out col);
            but.BackgroundColor = col;
            if (clicked == null) clicked = but;
            else
            {
                if (col.Equals(clicked.BackgroundColor))
                {
                    but.IsEnabled = false;
                    clicked.IsEnabled = false;
                    clicked = null;
                }
                else
                {
                    for(int i = 0; i < 24; i++)
                    {
                        (but.Parent.FindByName("f" + i) as Button).IsEnabled = false;
                    }
                    
                    Device.StartTimer(TimeSpan.FromSeconds(2), () =>
                    {

                        but.IsEnabled = true;
                        clicked.IsEnabled = true;
                        clicked.BackgroundColor = Color.Black;
                        but.BackgroundColor = Color.Black;
                        clicked = null;
                        for (int i = 0; i < 24; i++)
                        {
                            if((but.Parent.FindByName("f" + i) as Button).BackgroundColor == Color.Black) (but.Parent.FindByName("f" + i) as Button).IsEnabled = true;
                        }
                        return false;
                    });
                    
                }
            }               
        }
    }
    static class MyExtensions
    {
        private static Random rng = new Random();

        public static void Shuffle<T>(this IList<T> list)
        {
            int n = list.Count;
            while (n > 1)
            {
                n--;
                int k = rng.Next(n + 1);
                T value = list[k];
                list[k] = list[n];
                list[n] = value;
            }
        }
    }
}