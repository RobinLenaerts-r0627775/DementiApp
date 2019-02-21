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
        int score = 0;
        Dictionary<string, Color> layout = new Dictionary<string, Color>();
        Dictionary<string, string> pics = new Dictionary<string, string>();
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
                        pics.Add("" + i, "Luci.jpg");
                        break;
                    case 1:
                        layout.Add("" + i, Color.Magenta);
                        pics.Add("" + i, "Luci.jpg");
                        break;
                    case 2:
                        layout.Add("" + i, Color.LimeGreen);
                        pics.Add("" + i, "Decker.jpg");
                        break;
                    case 3:
                        layout.Add("" + i, Color.LimeGreen);
                        pics.Add("" + i, "Decker.jpg");
                        break;
                    case 4:
                        layout.Add("" + i, Color.Yellow);
                        pics.Add("" + i, "Maze.jpg");
                        break;
                    case 5:
                        layout.Add("" + i, Color.Yellow);
                        pics.Add("" + i, "Maze.jpg");
                        break;
                    case 6:
                        layout.Add("" + i, Color.Green);
                        pics.Add("" + i, "Linda.jpg");
                        break;
                    case 7:
                        layout.Add("" + i, Color.Green);
                        pics.Add("" + i, "Linda.jpg");
                        break;
                    case 8:
                        layout.Add("" + i, Color.OrangeRed);
                        pics.Add("" + i, "Dan.jpg");
                        break;
                    case 9:
                        layout.Add("" + i, Color.OrangeRed);
                        pics.Add("" + i, "Dan.jpg");
                        break;
                    case 10:
                        layout.Add("" + i, Color.Brown);
                        pics.Add("" + i, "Amenadiel.jpg");
                        break;
                    case 11:
                        layout.Add("" + i, Color.Brown);
                        pics.Add("" + i, "Amenadiel.jpg");
                        break;
                    case 12:
                        layout.Add("" + i, Color.Gray);
                        pics.Add("" + i, "Ella.jpg");
                        break;
                    case 13:
                        layout.Add("" + i, Color.Gray);
                        pics.Add("" + i, "Ella.jpg");
                        break;
                    case 14:
                        layout.Add("" + i, Color.MidnightBlue);
                        pics.Add("" + i, "Nolan.jpg");
                        break;
                    case 15:
                        layout.Add("" + i, Color.MidnightBlue);
                        pics.Add("" + i, "Nolan.jpg");
                        break;
                    case 16:
                        layout.Add("" + i, Color.Moccasin);
                        pics.Add("" + i, "Talia.jpg");
                        break;
                    case 17:
                        layout.Add("" + i, Color.Moccasin);
                        pics.Add("" + i, "Talia.jpg");
                        break;
                    case 18:
                        layout.Add("" + i, Color.YellowGreen);
                        pics.Add("" + i, "Trixie.jpg");
                        break;
                    case 19:
                        layout.Add("" + i, Color.YellowGreen);
                        pics.Add("" + i, "Trixie.jpg");
                        break;
                    case 20:
                        layout.Add("" + i, Color.Olive);
                        pics.Add("" + i, "Lucy.jpg");
                        break;
                    case 21:
                        layout.Add("" + i, Color.Olive);
                        pics.Add("" + i, "Lucy.jpg");
                        break;
                    case 22:
                        layout.Add("" + i, Color.Orange);
                        pics.Add("" + i, "Tim.jpg");
                        break;
                    case 23:
                        layout.Add("" + i, Color.Orange);
                        pics.Add("" + i, "Tim.jpg");
                        break;
                    default:
                        layout.Add("" + i, Color.Black);
                        pics.Add("" + i, "Luci.jpg");
                        break;
                    }

            }
			InitializeComponent ();
		}

        private void Button_Clicked(object sender, EventArgs e)
        {
            Button but = (Button)sender;
            but.IsEnabled = false;
            but.BorderColor = Color.LightYellow;
            Color col = Color.FromHex("#372c73");
            layout.TryGetValue(but.StyleId, out col);
            but.BackgroundColor = col;
            String pic = "";
            pics.TryGetValue(but.StyleId, out pic);
            but.Image = pic;
            if (clicked == null) clicked = but;
            else
            {
                if (col.Equals(clicked.BackgroundColor))
                {
                    but.IsEnabled = false;
                    clicked.IsEnabled = false;
                    but.BorderColor = Color.Transparent;
                    but.BackgroundColor = Color.Transparent;
                    clicked.BackgroundColor = Color.Transparent;
                    clicked.BorderColor = Color.Transparent;
                    clicked = null;
                    score += 1;
                    if (score == 12)
                    {
                        Navigation.PushAsync(new WinPage());
                    }

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
                        but.BorderColor = Color.Transparent;
                        clicked.BorderColor = Color.Transparent;
                        clicked.BackgroundColor = Color.FromHex("#372c73");
                        clicked.Image = null;
                        but.BackgroundColor = Color.FromHex("#372c73");
                        but.Image = null;
                        clicked = null;
                        for (int i = 0; i < 24; i++)
                        {
                            if((but.Parent.FindByName("f" + i) as Button).BackgroundColor == Color.FromHex("#372c73")) (but.Parent.FindByName("f" + i) as Button).IsEnabled = true;
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